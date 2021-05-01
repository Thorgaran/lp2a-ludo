package ludoGame;

import java.awt.Color;
import java.util.*;

import javax.swing.JLabel;

import com.therolf.miniServer.Client;
import com.therolf.miniServer.MessageType;

public class Game {
	// Define game colors only once here, from north west to north east
	public static Color NW_COLOR = new Color(255, 245, 0);
	public static Color SW_COLOR = Color.CYAN;
	public static Color SE_COLOR = Color.GREEN;
	public static Color NE_COLOR = Color.RED;
	public static Color[] PLAYER_COLORS = {Game.NW_COLOR, Game.SW_COLOR, Game.SE_COLOR, Game.NE_COLOR};
	
	// This constant defines the number of AI games to be played when comparing two
	public static int NB_AI_GAMES = 10000;
	
	// The player types represented as indexes (because they are coming straight from comboBoxes)
	// Needs to be static because it's used directly by action listeners
	private static HashMap<Color, PlayerType> playerTypes = new HashMap<Color, PlayerType>();
	
	private static JLabel infoText;
	
	private static Client client = null;
	
	private LinkedHashMap<Color, Player> players = new LinkedHashMap<Color, Player>();
	private Dice dice;
	
	Game(Menu menu) {
		Board board = menu.getBoard();
		Game.infoText = menu.getInfoText();
		
		// This class is basically a struct only used for the board creation
		class SquareInitData {
			public SquareType type;
			public int row;
			public int col;
			
			SquareInitData(SquareType type, int row, int col) {
				this.type = type;
				this.row = row;
				this.col = col;
			}
		}
		
		SquareInitData[] path = {
			new SquareInitData(SquareType.Normal, 0, 6),
			new SquareInitData(SquareType.Normal,  1, 6),
			new SquareInitData(SquareType.Safe, 2, 6),
			new SquareInitData(SquareType.Normal, 3, 6),
			new SquareInitData(SquareType.Normal, 4, 6),
			new SquareInitData(SquareType.Normal, 5, 6),
			new SquareInitData(SquareType.Normal, 6, 5),
			new SquareInitData(SquareType.Normal, 6, 4),
			new SquareInitData(SquareType.Normal, 6, 3),
			new SquareInitData(SquareType.Normal, 6, 2),
			new SquareInitData(SquareType.Start, 6, 1),
			new SquareInitData(SquareType.Normal, 6, 0),
			new SquareInitData(SquareType.Fork, 7, 0),
		};
		
		SquareInitData[] home = {
			new SquareInitData(SquareType.Home, 1, 1),
			new SquareInitData(SquareType.Home, 1, 3),
			new SquareInitData(SquareType.Home, 3, 1),
			new SquareInitData(SquareType.Home, 3, 3),
		};
		
		HashMap<Color, Direction> arrowDirs = new HashMap<Color, Direction>();
		arrowDirs.put(Game.PLAYER_COLORS[0], Direction.East);
		arrowDirs.put(Game.PLAYER_COLORS[1], Direction.North);
		arrowDirs.put(Game.PLAYER_COLORS[2], Direction.West);
		arrowDirs.put(Game.PLAYER_COLORS[3], Direction.South);
		
		// This firstSquare variable is used later on to close the loop
		boolean isFirstSquare = true;
		Square firstSquare = null;
		
		// Build the board
		Square nextSquare = null;
		
		for(Color color: Game.PLAYER_COLORS) {
			// Initialize players to full human
			Game.playerTypes.put(color, PlayerType.HumanPlayer);
			
			for(SquareInitData pathData: path) {				
				// Create the next square in counterclockwise order
				if (pathData.type == SquareType.Fork) {
					nextSquare = new ForkSquare(nextSquare, color, pathData.row, pathData.col, arrowDirs.get(color));
					
					// Travel the home row to add squares in the board
					Square goalRowSquare = ((ForkSquare) nextSquare).getGoalRowSquare();
					
					while (goalRowSquare != null) {
						board.addSquare(goalRowSquare);
						goalRowSquare = goalRowSquare.getNextSquare();
					}
				}
				else if (pathData.type == SquareType.Start) {
					nextSquare = new StartSquare(nextSquare, color, pathData.row, pathData.col, arrowDirs.get(color));
				}
				else {
					nextSquare = new Square(nextSquare, pathData.type, color, pathData.row, pathData.col);
				}
				
				if (isFirstSquare) {
					isFirstSquare = false;
					firstSquare = nextSquare;
				}
				else if (pathData.type == SquareType.Start) {
					// When reaching a starting square, create the corresponding player and its home squares
					ArrayList<Square> homes = new ArrayList<Square>();
					
					for(SquareInitData homeData: home) {
						Square homeSquare = new Square(nextSquare, homeData.type, color, homeData.row, homeData.col);
						homes.add(homeSquare);
						board.addSquare(homeSquare);
					}
					
					Player player = new HumanPlayer(color, homes);
					players.put(color, player);
				}
				
				// Add the square in the board
				board.addSquare(nextSquare);
				
				// Prepare the position for the next quadrant
				int[] newPathPos = this.rotatePos(pathData.row, pathData.col);
				pathData.row = newPathPos[0];
				pathData.col = newPathPos[1];
			}
			
			for(SquareInitData homeData: home) {
				int[] newHomePos = this.rotatePos(homeData.row, homeData.col);
				homeData.row = newHomePos[0];
				homeData.col = newHomePos[1];
			}
		}
		
		// Close the loop
		firstSquare.setNextSquare(nextSquare);
		
		// Create the skip buttons
		int[] coordinates={
			5, 5,
			5, 9,
			9, 9,
			9, 5
		};
		
		for(int i = 0; i<8; i+=2) {
			players.get(Game.PLAYER_COLORS[i/2]).setButton(board.addSkip(coordinates[i], coordinates[i+1]));
		}
		
		// Create the dice and its displays
		this.dice = new Dice(board, this.players.values());
	}
	
	// Transforms a row and a column to the same position in the next quadrant
	private int[] rotatePos(int row, int col) {
		int[] newPos = {
			-col + 14,
			row
		};
		
		return newPos;
	}
	
	// Full player turn, may include multiple player moves if the player rolls a six
	private void playerTurn(boolean showBoard, Player p) {
		int diceResult;
		int consecutiveTurns=1;
		
		do {
			if (Game.isMultiplayer() ) {
				if (p.getType() != PlayerType.RemotePlayer) {
					// If the player throwing isn't a remote player, send request for dice roll
					// (this check is needed to avoid multiple dice roll requests at the same time)
					Game.getClient().send(MessageType.AskRoll);
				}

				diceResult = this.dice.serverRoll();
			}
			else {
				diceResult = this.dice.roll();
			}
			
			this.dice.hideDices(showBoard);
			this.dice.dispFace(showBoard, p, 
				p.getType() == PlayerType.HumanPlayer || p.getType() == PlayerType.RemotePlayer);
			
			if (diceResult == 6 && consecutiveTurns == 3) { break; }
			
			p.turn(showBoard, diceResult);
						
			consecutiveTurns++;
		} while (diceResult == 6 && consecutiveTurns < 4);
	}
	
	// Returns the list of player colors from winner to loser
	public ArrayList<Color> play(boolean showBoard) {
		List<Color> turnOrder = new ArrayList<Color>(Arrays.asList(
			Game.NE_COLOR, Game.SE_COLOR, Game.SW_COLOR, Game.NW_COLOR
		));
		ArrayList<Color> winOrder = new ArrayList<Color>();
		
		Color currentColor = this.startingPlayer(showBoard, turnOrder); 
		while (turnOrder.size() > 1) {
			this.playerTurn(showBoard, this.players.get(currentColor));
			
			if (this.players.get(currentColor).hasWon()){
				turnOrder.remove(currentColor);
				winOrder.add(currentColor);
				
				if (showBoard) {					
					dice.changeToMedal(currentColor, 4-turnOrder.size());
				}
			}
			
			currentColor = turnOrder.get((turnOrder.indexOf(currentColor) + 1) % turnOrder.size());
		}
		
		// Display the winner and wait for the user to see
		Game.setInfoText(showBoard, this.players.get(winOrder.get(0)).getColoredType() + " won, congrats! The game is over!");
		Game.sleep(showBoard, 3000);
		
		// Reset the game to its initial state
		
		if (showBoard) {
			this.dice.reset();
		}
		
		for(Player player: this.players.values()) {
			player.reset(showBoard);
		}
		
		return winOrder;
	}
	
	// Gets a random starting player following the real-life algorithm
	public Color startingPlayer(boolean showBoard, List<Color> playerColors) {
		LinkedHashMap<Color, Integer> colorsWithThrow = new LinkedHashMap<Color, Integer>();
		
		// Initialization of the colors in the hashmap is needed
		for(Color color: playerColors) {
			colorsWithThrow.put(color, null);
		}
		
		Game.setInfoText(showBoard, "Selection of the first player...");
		Game.sleep(showBoard, 1000);
		
		while (colorsWithThrow.size() != 1) {
			// Have the remaining players throw their dices
			for(Color color: colorsWithThrow.keySet()) {
				if (Game.isMultiplayer()) {
					if (this.players.get(color).getType() != PlayerType.RemotePlayer) {
						// If the player throwing isn't a remote player, send request for dice roll
						// (this check is needed to avoid multiple dice roll requests at the same time)
						Game.getClient().send(MessageType.AskRoll);
					}
					colorsWithThrow.put(color, this.dice.serverRoll());
				}
				else {
					colorsWithThrow.put(color, this.dice.roll());
				}
				
				this.dice.dispFace(showBoard, this.players.get(color), true);
			}
			
			// Get the biggest throw value
			int biggestThrow = Collections.max(colorsWithThrow.values());
			
			// Remove any players under this value
			// Using an iterator here is instead of a for loop is 
			// needed to avoid a ConcurrentModificationException
			Iterator<Map.Entry<Color, Integer>> it = colorsWithThrow.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<Color, Integer> entry = it.next();
				if (entry.getValue() < biggestThrow) {
					it.remove();
				}
			}
			
			// Add some wait time for the user to see who has the highest throw
			Game.sleep(showBoard, 2000);
			
			// Hide all dices
			this.dice.hideDices(showBoard);
		}
		
		// Return the first player's color
		return colorsWithThrow.keySet().iterator().next();
	}
	
	public static Client getClient() {
		return Game.client;
	}
	
	public static void setClient(Client client) {
		Game.client = client;
	}
	
	public Player getPlayer(Color playerColor) {
		return this.players.get(playerColor);
	}
	
	public static void setPlayerType(Color playerColor, PlayerType type) {
		Game.playerTypes.put(playerColor, type);
	}
	
	public static PlayerType getPlayerType(Color playerColor) {
		return Game.playerTypes.get(playerColor);
	}
	
	// Transforms each player object into the new inherited type depending on Game.playerTypes
	public void updatePlayerTypes() {
		for(Player player: this.players.values()) {
			this.players.put(
				player.getColor(), 
				Game.playerTypes.get(player.getColor()).turnPlayerInto(player)
			);
		}
	}
	
	// Sets the text to be displayed at the top of the board during a game
	public static void setInfoText(boolean showBoard, String text) {
		if (showBoard) {
			Game.infoText.setText("<html>" + text + "</html>");
		}
	}
	
	public static boolean isMultiplayer() {
		return Game.client != null;
	}
	
	// Only works if the text is displayed in a container that supports HTML
	public static String dyeText(String text, Color color) {
		float[] colorComp = color.getRGBColorComponents(null);
		String rgbString = "rgb(" + (int) (colorComp[0]*255) + "," 
								  + (int) (colorComp[1]*255) + "," 
								  + (int) (colorComp[2]*255) + ")";
		
		return "<span style=\"color:" + rgbString + ";\">" + text + "</span>";
	}
	
	// Simple wrapper around Thread.sleep()
	public static void sleep(boolean showBoard, long milli) {
		if (showBoard) {			
			try {
				Thread.sleep(milli);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}

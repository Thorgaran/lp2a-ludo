package ludoGame;

import java.awt.Color;
import java.util.*;

public class Game {
	// Define game colors only once here, from north west to north east
	public static Color NW_COLOR = new Color(255, 245, 0);
	public static Color SW_COLOR = Color.CYAN;
	public static Color SE_COLOR = Color.GREEN;
	public static Color NE_COLOR = Color.RED;
	
	LinkedHashMap<Color, Player> players = new LinkedHashMap<Color, Player>();
	private Dice dice;
	
	Game(Board board) {
		Color[] playerColors = {Game.NW_COLOR, Game.SW_COLOR, Game.SE_COLOR, Game.NE_COLOR};
		HashMap<Color, Boolean> playerType = new HashMap<Color, Boolean>();
		playerType.put(playerColors[0], false);
		playerType.put(playerColors[1], false);
		playerType.put(playerColors[2], false);
		playerType.put(playerColors[3], false);
		
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
		arrowDirs.put(playerColors[0], Direction.East);
		arrowDirs.put(playerColors[1], Direction.North);
		arrowDirs.put(playerColors[2], Direction.West);
		arrowDirs.put(playerColors[3], Direction.South);
		
		// This firstSquare variable is used later on to close the loop
		boolean isFirstSquare = true;
		Square firstSquare = null;
		
		// Build the board
		Square nextSquare = null;
		
		for(Color color: playerColors) {
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
					
					Player player = (playerType.get(color)) 
						? new HumanPlayer(color, homes) 
						: new RandomAI(color, homes);
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
			players.get(playerColors[i/2]).setButton(board.addSkip(coordinates[i], coordinates[i+1]));
		}
		
		// Create the dice and its displays
		this.dice = new Dice(board, this.players.values());

		//board.repaint();
	}
	
	// Transforms a row and a column to the same position in the next quadrant
	private int[] rotatePos(int row, int col) {
		int[] newPos = {
			-col + 14,
			row
		};
		
		return newPos;
	}

	private void playerTurn(Player p) {
		int diceResult;
		int consecutiveTurns=1;
		
		do {
			diceResult=this.dice.roll();
			this.dice.dispFace(p.getColor());
			
			if (diceResult == 6 && consecutiveTurns == 3) { break; }
			
			p.turn(diceResult);
						
			consecutiveTurns++;
		} while (diceResult == 6 && consecutiveTurns < 4);
	}
	
	public void play() {
		List<Color> turnOrder = new ArrayList<Color>(Arrays.asList(
			Game.NE_COLOR, Game.SE_COLOR, Game.SW_COLOR, Game.NW_COLOR
		)); 
		Color currentColor = this.starter(turnOrder); 
		
		while (turnOrder.size() > 1) {
			this.playerTurn(this.players.get(currentColor));
			
			if (this.players.get(currentColor).hasWon()){
				turnOrder.remove(currentColor);
				
				dice.changeToMedal(currentColor, 4-turnOrder.size());
			}
			
			currentColor = turnOrder.get((turnOrder.indexOf(currentColor) + 1) % turnOrder.size());
		}
		System.out.println("Game ended!");
	}

	public Color starter(List<Color> l) {
		Color toReturn = Color.RED;
		int bestRoll, newRoll;
		
		System.out.println(Game.colorToString(l.get(0)) + " player rolls the dice:");
		bestRoll = this.dice.roll();
		//this.dice.dispFace();
		
		for(int i=1; i<4; i++){
			do {
			System.out.println(Game.colorToString(l.get(i)) + " player rolls the dice:");
			newRoll = this.dice.roll();
			//this.dice.dispFace();
			if (newRoll == bestRoll) {
				System.out.println("Draw! Please roll again!");
			}
			} while (newRoll == bestRoll);
			if (newRoll>bestRoll) {
				bestRoll = newRoll;
				toReturn = l.get(i);
			}
		}
		System.out.println(Game.colorToString(toReturn) + " begins!");
		return toReturn;
	}
	
	public static String colorToString(Color color) {
		if (color == Game.NE_COLOR) {
			return "Red";
		} else if (color == Game.SE_COLOR) {
			return "Green";
		} else if (color == Game.SW_COLOR) {
			return "Blue";
		} else  if (color == Game.NW_COLOR) {
			return "Yellow";
		} else {
			System.out.println("Illegal player color!");
			return "?";
		}
	}

	public static void main(String[] args) {
		Window window = new Window();
		
		Board board = window.setupBoard();
		
		Game game = new Game(board);
		window.rebuild();
		
		game.play();
	}

}

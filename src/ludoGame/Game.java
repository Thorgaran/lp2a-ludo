package ludoGame;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

public class Game {
	LinkedHashMap<Color, Player> players = new LinkedHashMap<Color, Player>();
	private Dice dice = new Dice();
	
	Game() {
		Color[] playerColors = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED};
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
		
		// This firstSquare variable is used later on to close the loop
		boolean isFirstSquare = true;
		Square firstSquare = null;
		
		// Build the board
		Square nextSquare = null;
		for(Color color: playerColors) {
			for(SquareInitData pathData: path) {				
				// Create the next square in counterclockwise order
				if (pathData.type == SquareType.Fork) {
					nextSquare = new ForkSquare(nextSquare, color, pathData.row, pathData.col);
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
						homes.add(new Square(nextSquare, homeData.type, color, homeData.row, homeData.col));
					}
					
					Player player = (playerType.get(color)) ? new HumanPlayer(color, homes) : new RandomAI(color, homes);
					players.put(color, player);
				}
				
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
	}
	
	public void printBoard() {
		Square[][] board = new Square[15][15];
		
		Square startSquare = this.players.get(Color.RED).getStartSquare();
		Square curSquare = startSquare;
		
		do {
			// If the current square is a fork, travel its associated goal row
			if (curSquare.getType() == SquareType.Fork) {
				// We now know curSquare is a fork, thus we can tell the compiler to consider curSquare as a ForkSquare
				Square goalRowSquare = ((ForkSquare) curSquare).getGoalRowSquare();
				
				for(int i=0; i<6; i++) {
					board[goalRowSquare.getRow()][goalRowSquare.getCol()] = goalRowSquare;
					goalRowSquare = goalRowSquare.getNextSquare();
				}
			}
			else if (curSquare.getType() == SquareType.Start) {
				for(Square homeSquare: this.players.get(curSquare.getColor()).getHomeSquares()) {
					board[homeSquare.getRow()][homeSquare.getCol()] = homeSquare;
				}
			}
			
			board[curSquare.getRow()][curSquare.getCol()] = curSquare;
			
			curSquare = curSquare.getNextSquare();
		} while (curSquare != startSquare);
		
		System.out.println("    A  B  C  D  E  F  G  H  I  J  K  L  M  N  O");
		int nbRow = 1;
		for(Square[] row: board) {
			for (int subRow = 0; subRow < 2; subRow++) {
				if (subRow == 0) {
					System.out.printf("%2d  ", nbRow);
				}
				else {
					System.out.print("    ");
				}
				
				for(Square square: row) {
					if (square == null) {
						System.out.print("   ");
					}
					else {
						System.out.print(square.toString(subRow));
					}
				}
				System.out.println();
			}
			nbRow++;
		} 
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
			this.printBoard();

			diceResult=this.dice.roll();
			this.dice.dispFace();
				
			System.out.println("Current player: " + Game.colorToString(p.getColor()));
			System.out.println("Has eaten: " + p.hasEaten());
			
			if (diceResult == 6 && consecutiveTurns == 3) { break; }
			
			p.turn(diceResult);
						
			consecutiveTurns++;
		} while (diceResult == 6 && consecutiveTurns < 4);
	}
	
	public void play() {
		List<Color> turnOrder = new ArrayList<Color>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)); 
		Color currentColor = this.starter(turnOrder); 
		
		while (turnOrder.size() > 1) {
			this.playerTurn(this.players.get(currentColor));
			
			if (this.players.get(currentColor).hasWon()){
				turnOrder.remove(currentColor);
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
		this.dice.dispFace();
		
		for(int i=1; i<4; i++){
			do {
			System.out.println(Game.colorToString(l.get(i)) + " player rolls the dice:");
			newRoll = this.dice.roll();
			this.dice.dispFace();
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
		if (color == Color.RED) {
			return "Red";
		} else if (color == Color.GREEN) {
			return "Green";
		} else if (color == Color.BLUE) {
			return "Blue";
		} else  if (color == Color.YELLOW) {
			return "Yellow";
		} else {
			System.out.println("Illegal player color!");
			return "?";
		}
	}
	
	public static char colorToChar(Color color) {
		return Game.colorToString(color).charAt(0);
	}

	public static void main(String[] args) {
		Game game = new Game();
		
		game.play();
	}

}

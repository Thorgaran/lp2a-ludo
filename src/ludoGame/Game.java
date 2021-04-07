package ludoGame;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

public class Game {
	LinkedHashMap<Color, Player> players = new LinkedHashMap<Color, Player>();
	private Dice dice = new Dice();
	
	Game() {
		Color[] playerColors = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED};
		
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
					
					Player player = new Player(color, homes);
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
		
		for(Square[] row: board) {
			for (int subRow = 0; subRow < 3; subRow++) {
				for(Square square: row) {
					if (square == null) {
						System.out.print("     ");
					}
					else {
						System.out.print(square.toString(subRow));
					}
				}
				System.out.println();
			}
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
				
			System.out.println("Current player: " + Game.colorToChar(p.getColor()));
			
			if (diceResult == 6 && consecutiveTurns == 3) { break; }
			
			p.turn(diceResult);
						
			consecutiveTurns++;
		} while (diceResult == 6 && consecutiveTurns < 4);
	}
	
	public void play() {
		List<Color> turnOrder = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW); 
		Color currentColor = Color.GREEN; // TODO: get the starting player with random dice throws
		
		while (true) {
			playerTurn(this.players.get(currentColor));
			
			currentColor = turnOrder.get((turnOrder.indexOf(currentColor) + 1) % 4);
		}
	}

	public static char colorToChar(Color color) {
		if (color == Color.RED) {
			return 'R';
		} else if (color == Color.GREEN) {
			return 'G';
		} else if (color == Color.BLUE) {
			return 'B';
		} else  if (color == Color.YELLOW) {
			return 'Y';
		} else {
			System.out.println("Illegal player color!");
			return '?';
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		
		game.play();
	}

}

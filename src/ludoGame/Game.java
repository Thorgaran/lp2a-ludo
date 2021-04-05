package ludoGame;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;

public class Game {
	HashMap<Color, Player> players = new HashMap<Color, Player>();
	
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
		
		// This firstSquare variable is used later on to close the loop
		boolean isFirstSquare = true;
		Square firstSquare = null;
		
		// Build the board
		Square nextSquare = null;
		for(Color color: playerColors) {
			for(SquareInitData squareData: path) {				
				// Create the next square in counterclockwise order
				if (squareData.type == SquareType.Fork) {
					nextSquare = new ForkSquare(nextSquare, color, squareData.row, squareData.col);
				}
				else {
					nextSquare = new Square(nextSquare, squareData.type, color, squareData.row, squareData.col);
				}
				
				// Save row position for the following computation
				int oldRow = squareData.row;
				
				// Flip the square position to the next quadrant
				squareData.row = -squareData.col + 14;
				squareData.col = oldRow;
				
				if (isFirstSquare) {
					isFirstSquare = false;
					firstSquare = nextSquare;
				}
				else if (squareData.type == SquareType.Start) {
					// When reaching a starting square, create the corresponding player
					Player player = new Player(color, nextSquare);
					players.put(color, player);
				}
			}
		}
		
		// Close the loop
		firstSquare.setNextSquare(nextSquare);
	}
	
	public void printBoard() {
		char[][] charBoard = new char[15][15];
		
		// Fill each row with spaces
		for (char[] row: charBoard) {
			Arrays.fill(row, ' ');
		}
		
		Square startSquare = this.players.get(Color.RED).getStartSquare();
		Square curSquare = startSquare;
		Square savedFork = null;
		do {
			char colorLetter;
			if (curSquare.getColor() == Color.RED) {
				colorLetter = 'R';
			} else if (curSquare.getColor() == Color.GREEN) {
				colorLetter = 'G';
			} else if (curSquare.getColor() == Color.BLUE) {
				colorLetter = 'B';
			} else {
				colorLetter = 'Y';
			}
			
			charBoard[curSquare.getRow()][curSquare.getCol()] = colorLetter;
			
			// Get the next square, going into the home row when encountering a fork
			if (curSquare.getType() == SquareType.Fork) {
				savedFork = curSquare;
				// We now know curSquare is a fork, thus we can tell the compiler to consider curSquare as a ForkSquare
				curSquare = ((ForkSquare) curSquare).getHomeSquare();
			}
			else {
				curSquare = curSquare.getNextSquare();
				
				// If curSquare is null, that means we reached past a goal square, thus we return to the fork
				if (curSquare == null) {
					curSquare = savedFork.getNextSquare();
				}
			}
		} while (curSquare != startSquare);
		
		for (char[] row: charBoard) {
			for (char c: row) {
				System.out.print(c);
			}
			System.out.println();
		} 
	}
	
	public Square moveForward(Square ogPosition, int movNb,Token t) { //tatakae, tatakae
		Square toReturn = ogPosition;
		
		if (toReturn.getType()==SquareType.Home) { 
			//we check if the token is still trying to get out of its home
			
			if (movNb==6) {
				toReturn = toReturn.getNextSquare();
			} else {
				toReturn = null;
			}
		
		} else {
			//the token is already in play
			
			for (int i=0;i < movNb; i++) {
				
				if (toReturn.getType()==SquareType.Fork && toReturn.getColor()==t.getPlayer().getColor()) {
					//case diverges if we are entering the goal row
					toReturn = ((ForkSquare)toReturn).getHomeSquare();
				} else {
					toReturn = toReturn.getNextSquare();
				}
				
				/*if (toReturn.getTokens().isEmpty()!=true && condition bloquante) {
				 * 	if (i!=movNb-1){
				 * 		return null;
				 * 	} else {
				 * 		empilage des pions a definir
				 * 	}
				 * }*/
				
				/*el*/if (toReturn.getType()==SquareType.Goal) {
					return toReturn; //we break the for instantly because the token reached the end
				}
			
			}
		
		}
		
		return toReturn;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		
		game.printBoard();
	}

}

package ludoGame;

import java.awt.Color;

public class ForkSquare extends Square {
	private Square homeSquare;
	
	ForkSquare(Square nextSquare, Color c, int row, int col) {
		super(nextSquare, SquareType.Fork, c, row, col);
		
		// Get corresponding goal coordinates
		switch (row) {
			case 0:
				row = 6;
				break;
				
			case 7:
				col = (col == 0) ? 6 : 8;
				break;
				
			case 14:
				row = 8;
				break;
		}
		
		// Create goal square
		Square next = new Square(null, SquareType.Goal, c, row, col);
		
		for(int i=0; i<5; i++) {
			// Move coordinates one square away from the center
			row = this.getFurtherFromSeven(row);
			col = this.getFurtherFromSeven(col);
			
			// Create home row square
			next = new Square(next, SquareType.Home, c, row, col);
		}
		
		this.homeSquare = next;
	}
	
	public Square getHomeSquare() {
		return this.homeSquare;
	}
	
	private int getFurtherFromSeven(int n) {
		if (n == 7) {
			return 7;
		}
		else if (n < 7) {
			return n - 1;
		}
		else {
			return n + 1;
		}
	}
}

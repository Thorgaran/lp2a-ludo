package ludoGame;

import java.awt.Color;

@SuppressWarnings("serial")
public class StartSquare extends Square {
	private Direction arrowDir;
	
	StartSquare(Square nextSquare, Color c, int row, int col, Direction arrowDir) {
		super(nextSquare, SquareType.Start, c, row, col);
		
		this.arrowDir = arrowDir;
	}
	
	public Direction getArrowDir() {
		return this.arrowDir;
	}
}

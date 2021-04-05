package ludoGame;
import java.awt.Color;
import java.util.ArrayList;

public class Square {
	private Square nextSquare;
	private Color color; // This color represents the quadrant the square is in
	private ArrayList<Token> tokens = new ArrayList<Token>(); // List of tokens starts out as empty
	private SquareType type;
	
	private int row;
	private int col;
	
	Square(Square nextSquare, SquareType t, Color c, int row, int col) {
		this.nextSquare = nextSquare;
		this.type = t;
		this.color = c;
		this.row = row;
		this.col = col;
	}
	
	Square() {
		System.out.println("Incorrect square initialization!");
	}
	
	public Square getNextSquare() {
		return this.nextSquare;
	}
	
	protected void setNextSquare(Square nextSquare) {
		this.nextSquare = nextSquare;
	}
	
	public void addToken(Token token) {
		this.tokens.add(token);
	}
	public ArrayList<Token> getTokens() {
		return this.tokens;
	}
	
	public SquareType getType() {
		return this.type;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
}

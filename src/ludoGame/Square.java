package ludoGame;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

public class Square extends JPanel {
	private Square nextSquare;
	private Color color; // This color represents the quadrant the square is in
	private ArrayList<Token> tokens = new ArrayList<Token>(); // List of tokens starts out as empty
	private SquareType type;
	
	private int row;
	private int col;
	
	Square(Square nextSquare, SquareType type, Color color, int row, int col) {
		this.nextSquare = nextSquare;
		this.type = type;
		this.color = color;
		this.row = row;
		this.col = col;
		
		this.setLayout(null);
		
		if (type == SquareType.Normal || type == SquareType.Fork) {
			this.setBackground(Color.WHITE);
		}
		else {
			this.setBackground(color);
		}

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
		this.add(token, 0);
	}
	
	public void removeToken(Token token) {
		this.tokens.remove(token);
		this.remove(token);
	}
	
	public ArrayList<Token> getTokens() {
		return this.tokens;
	}
	
	public int nbTokens() {
		return this.tokens.size();
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
	
	// Returns true if that square prevents tokens from going past
	public boolean isBlocking() {
		if (this.type == SquareType.Goal) {
			// You can't go past the goal
			return true;
		}
		else if (this.type == SquareType.GoalRow && !this.tokens.isEmpty()) { 
			// You can't pass over your own pieces in the goal row
			return true;
		}
		else {
			// Any pile of tokens is blocking
			return this.tokens.size() > 1;	
		}
	}

	// After calling this function, we know that if the return value is true, there is only one eatable token in the square
	public boolean isEatable(Token activeToken) {
		if (this.getType() == SquareType.Safe || this.getType() == SquareType.Start) {
			return false;
		}
		else {
			switch (this.nbTokens()) {
			case 1:
				// Tokens can't eat tokens of the same color
				return this.getTokens().get(0).getColor() != activeToken.getColor();
			case 2:
				// Blocks can eat other blocks
				return this.getTokens().get(0).getColor() != activeToken.getColor() &&
						activeToken.isBlockBase() && 
						this.getTokens().get(0).isBlockBase();
			default:
				return false;
			}		
		}
	}
	
	public String toString(int row) {
		switch (row) {
		case 0: {
			StringBuffer str = new StringBuffer(".  ");
			int i = 0;
			while (i < this.tokens.size() && i < 3) {
				str.setCharAt(i, Game.colorToChar(this.tokens.get(i).getColor()));
				i++;
			}
			return str.toString();
		}
		case 1: {
			StringBuffer str = new StringBuffer(this.type.toChar() + "  ");
			int i = 3;
			while (i < this.tokens.size() && i < 5) {
				str.setCharAt(i - 2, Game.colorToChar(this.tokens.get(i).getColor()));
				i++;
			}			
			return str.toString();
		}
		default:
			System.out.println("Error, square string row index too big!");
			return "";
		}
	}
	
	public String toString() {		
		return this.toString(0) + "\n" + this.toString(1) + "\n" + this.toString(2);
	}
	
	public void paintComponent(Graphics g) {
		// Propagate painting chain (see https://www.oracle.com/java/technologies/painting.html)
		super.paintComponent(g);
	}
}

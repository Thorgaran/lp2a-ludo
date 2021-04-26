package ludoGame;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
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
	
	// Add a token both to the internal token list (game engine) and components list (GUI)
	public void addToken(Token token) {
		this.tokens.add(token);
		this.add(token, 0);
	}
	
	// Removes a token both from the internal token list (game engine) and components list (GUI)
	public void removeToken(Token token) {
		this.tokens.remove(token);
		this.remove(token);
	}
	
	public ArrayList<Token> getTokens() {
		return this.tokens;
	}
	
	// Returns the number of tokens currently present on the square
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

	// After calling this function, we know that if the return value is true, 
	// there is only one eatable token in the square (or a block)
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
	
	// Rotates x and y coordinates clockwise a certain number of times depending on dir
	private int[] rotateCoords(int x, int y, Direction dir) {
		// Rotate the coordinates 0 to 3 times
		switch (dir) {
		case East:
			int savedX1 = x;
			x = -y + 50;
			y = savedX1;
			
		case North:
			int savedX2 = x;
			x = -y + 50;
			y = savedX2;
		
		case West:
			int savedX3 = x;
			x = -y + 50;
			y = savedX3;
		
		case South:
			break;
		}
		
		int[] newCoords = { x, y };
		return newCoords;
	}
	
	// Returns an AWT shape in the form of an arrow pointing towards arrowDir
	private Shape createArrow(Direction arrowDir) {
		// Coordinates for a south-aiming arrow
		int[] coords = {
			27, 4,
			27, 38,
			40, 27,
			40, 32,
			25, 44,
			10, 32,
			10, 27,
			23, 38,
			23, 4
		};
		
		Polygon arrow = new Polygon();
		for(int i=0; i<18; i+=2) {
			int[] rotatedCoords = this.rotateCoords(coords[i], coords[i+1], arrowDir);
			arrow.addPoint(rotatedCoords[0], rotatedCoords[1]);
		}
		
		return arrow;
	}
	
	public void paintComponent(Graphics g) {
		// Propagate painting chain (see https://www.oracle.com/java/technologies/painting.html)
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if (this.type == SquareType.Safe) {
			Polygon star = new Polygon();
			star.addPoint(25, 2);
			star.addPoint(29, 20);
			star.addPoint(46, 20);
			star.addPoint(32, 29);
			star.addPoint(38, 45);
			star.addPoint(25, 35);
			star.addPoint(11, 45);
			star.addPoint(17, 29);
			star.addPoint(3, 20);
			star.addPoint(20, 20);
			
			g2d.setColor(Color.WHITE);
			g2d.fill(star);
		}
		else if (this.type == SquareType.Start) {
			g2d.setColor(Color.WHITE);
			g2d.fill(this.createArrow(((StartSquare) this).getArrowDir()));
		}
		else if (this.type == SquareType.Fork) {
			ForkSquare forkSquare = (ForkSquare) this;
			g2d.setColor(this.color);
			g2d.fill(this.createArrow(forkSquare.getArrowDir()));
			
			if (forkSquare.isGoalRowLocked()) {
				g2d.setColor(Color.GRAY);
				
				switch (forkSquare.getArrowDir()) {
				case East:
					g2d.fillRect(44, 0, 6, 50);
					break;
					
				case North:
					g2d.fillRect(0, 0, 50, 6);
					break;
				
				case West:
					g2d.fillRect(0, 0, 6, 50);
					break;
				
				case South:
					g2d.fillRect(0, 44, 50, 6);
					break;
				}
			}
		}
	}
}

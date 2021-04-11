package ludoGame;

import java.awt.Color;
import java.util.*;
import javax.swing.*;

public abstract class Player {
	private Color color;
	private ArrayList<Square> homeSquares;
	private Token[] tokens = new Token[4];
	private boolean hasEaten = false;
	
	private JButton button;
	
	protected static Scanner keyboard = new Scanner(System.in);
	
	Player(Color color, ArrayList<Square> homes) {
		this.color = color;
		this.homeSquares = homes;
		
		for(int i=0; i<4; i++) {
			tokens[i] = new Token(this);
		}
	}
	
	public JButton getButton() {
		return this.button;
	}
	public void setButton(JButton b) {
		this.button = b;
	}
	
	public ArrayList<Square> getHomeSquares() {
		return this.homeSquares;
	}
	public Square getEmptyHomeSquare() {
		for (Square square : this.homeSquares) {
			if (square.getTokens().size() == 0) {
				return square;
			}
		}
		System.out.println("No home square empty!");
		return null;
	}
	
	public Square getStartSquare() {
		return this.homeSquares.get(0).getNextSquare();
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public Token[] getTokens(){
		return this.tokens;
	}
	
	public boolean hasEaten() {
		return this.hasEaten;
	}
	
	abstract protected Token chooseToken(HashMap<Token, Square> playableTokens);
	
	public void turn(int diceResult) {
		HashMap<Token, Square> playableTokens = new HashMap<Token, Square>();
		
		for (Token t: this.getTokens()) { // Check your valid moves
			Square destSquare = t.moveForward(diceResult);
			
			if (destSquare != null) {
				playableTokens.put(t, destSquare);
			}
		}
		
		Token chosenToken = this.chooseToken(playableTokens);
		if (chosenToken != null) {
			Square startSquare = chosenToken.getPosition();
			Square destSquare = playableTokens.get(chosenToken);
			
			if (destSquare.isEatable(chosenToken)) {
				while (destSquare.nbTokens() > 0) {
					Token eatenToken = destSquare.getTokens().get(0);
					Square homeSquare = eatenToken.getPlayer().getEmptyHomeSquare();
					
					eatenToken.move(homeSquare);
					homeSquare.repaint();
				}
				this.hasEaten = true;
			}
			
			// This needs to be checked before moving the token
			boolean movingBlock = chosenToken.isBlockBase();
			
			chosenToken.move(destSquare);
			
			// If the token is a block base, move the token above it too
			if (movingBlock) {
				startSquare.getTokens().get(startSquare.nbTokens() - 1).move(destSquare);
			}
			
			// Repaint start and destination squares
			startSquare.repaint();
			destSquare.repaint();
		}
	}
	
	public boolean hasWon() {
		boolean hasWon = true;
		for(Token t: this.tokens) {
			if (t.getPosition().getType() != SquareType.Goal) {
				hasWon = false;
			}
		}
		
		if (hasWon == true) {
			System.out.println(Game.colorToString(this.color) + " player finished! Congratulations!");
		}
		
		return hasWon;
	}
}

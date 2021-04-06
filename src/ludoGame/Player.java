package ludoGame;

import java.awt.Color;
import java.util.*;

public class Player {
	private Color color;
	private ArrayList<Square> homeSquares;
	private Token[] tokens = new Token[4];
	private boolean hasEaten = false;
	
	Player(Color color, ArrayList<Square> homes) {
		this.color = color;
		this.homeSquares = homes;
		
		for(int i=0; i<4; i++) {
			tokens[i] = new Token(this);
		}
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
	
	public void setHasEaten() {
		this.hasEaten = true;
	}
	
	public boolean hasEaten() {
		return this.hasEaten;
	}
	
	private Token chooseToken(HashMap<Token, Square> playableTokens) {
		// For now, the player is a dumb AI who decides its move randomly
		Random generator = new Random();
		Token[] values = (Token[]) playableTokens.values().toArray();
		Token chosenToken = values[generator.nextInt(values.length)];
		
		return chosenToken;
	}
	
	public void turn(int diceResult) {
		HashMap<Token, Square> playableTokens = new HashMap<Token, Square>();
		
		for (Token t: this.getTokens()) { // Check your valid moves
			Square destSquare = t.moveForward(diceResult);
			
			if (destSquare != null) {
				playableTokens.put(t, destSquare);
			}
		}
		
		if (playableTokens.isEmpty()) {
			System.out.println("You can't play anything!");
		} else {
			Token chosenToken = this.chooseToken(playableTokens);
			Square destSquare = playableTokens.get(chosenToken);
			
			if (destSquare.isEatable() == true) {
				Token eatenToken = destSquare.getTokens().get(0);
				eatenToken.move(eatenToken.getPlayer().getEmptyHomeSquare());
				this.setHasEaten();
			}
			
			chosenToken.move(destSquare);
			
		}
	}
}

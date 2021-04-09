package ludoGame;

import java.awt.Color;

public class Token {
	private Square position;
	private Player player;
	private int height;
	
	Token() {
		System.out.println("Incorrect token initialization!");
	}
	
	Token(Player p) {
		this.player = p;
		this.height=0;
		this.position=p.getEmptyHomeSquare();
		this.position.addToken(this);
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Color getColor() {
		return this.player.getColor();
	}
	
	public Square getPosition() {
		return this.position;
	}
	
	public void setPosition(Square s) {
		this.position = s;
	}
	
	public void setHeight(int h) {
		this.height=h;
	}
	public int getHeight() {
		return this.height;
	}
	
	// True if the token is at the bottom of a pile of two with no other token on top
	public boolean isBlockBase() {
		// Condition order matters here
		return this.position.nbTokens() == this.height + 2 &&
				this.position.getTokens().get(this.height + 1).getColor() == this.getColor();
	}
	
	// A token is blocked if there is a token of another color above it, or two+ tokens of the same color
	public boolean isBlocked() {
		// If there are tokens above this one
		if (this.position.nbTokens() > this.height + 1) {
			// Then the token cannot move unless it is a block base
			return !this.isBlockBase();
		} 
		else {
			return false;
		}
	
	}
	
	// This function returns the square the token will be landing on, or null if the move is illegal
	public Square moveForward(int movNb) { //tatakae, tatakae
		Square toReturn = this.getPosition();
		
		if (toReturn.getType() == SquareType.Goal) {
			return null;
		}
		else if (toReturn.getType() == SquareType.Home) { 
			// We check if the token is still trying to get out of its home
			
			if (movNb == 6) {
				toReturn = toReturn.getNextSquare();
			} else {
				return null;
			}
		} 
		else if (this.isBlocked()) {
			return null;
		}
		else {
			// If the token is a block base, it can only move half the squares when the dice is even
			if (this.isBlockBase()) {
				if (movNb % 2 == 0) {
					movNb = movNb / 2;
				}
				else {
					return null;
				}
			}
			
			// The token is already in play and not blocked, thus it can try leaving its square
			for(int i=0; i < movNb; i++) {
				// Test if the token fulfills all conditions to enter the goal row
				if (toReturn.getType() == SquareType.Fork && 
					toReturn.getColor() == this.getColor() &&
					this.getPlayer().hasEaten() &&
					!this.isBlockBase())
				{
					toReturn = ((ForkSquare) toReturn).getGoalRowSquare();
				} else {
					toReturn = toReturn.getNextSquare();
				}
				
				if (toReturn.isBlocking() == true && i != movNb - 1) {
				  	return null;
				}
			}
			
			// You can't land on your own piece in the goal row
			if (toReturn.getType() == SquareType.GoalRow && toReturn.nbTokens() != 0) {
				return null;
			}
		
		}
		
		return toReturn;
	}
	
	public void move(Square dest) {
		this.setHeight(dest.getTokens().size());
		dest.addToken(this);
		this.position.removeToken(this);
		this.position=dest;
	}
}

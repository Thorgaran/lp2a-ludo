package ludoGame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Token extends JPanel implements MouseListener {
	private Square position;
	private Player player;
	private int height;
	
	// Only used for human player tokens
	private boolean isActive = false;
	
	Token() {
		System.out.println("Incorrect token initialization!");
	}
	
	Token(Player p) {
		this.player = p;
		this.height=0;
		this.position = p.getEmptyHomeSquare();
		this.position.addToken(this);
		
        this.addMouseListener(this);
        // Set size to make sure that the whole token is shown
        this.setSize(42, 19);
        // Set the token background to be transparent
        this.setOpaque(false);
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
	
	public void setTokHeight(int h) {
		this.height=h;
	}
	public int getTokHeight() {
		return this.height;
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
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
					this.getPlayer().hasEaten())
				{
					if (this.isBlockBase()) {
						return null;
					}
					else {
						toReturn = ((ForkSquare) toReturn).getGoalRowSquare();
					}
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
		this.setTokHeight(dest.getTokens().size());
		
		dest.addToken(this);
		
		this.position.removeToken(this);
		this.position=dest;
	}
	
	public void paintComponent(Graphics g) {
		// Propagate painting chain (see https://www.oracle.com/java/technologies/painting.html)
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// If there are more than 4 tokens on a square, display them smaller
		int squashHeight = 8;
		if (this.position.nbTokens() > 12) {
			squashHeight = 1;
		}
		else if (this.position.nbTokens() > 7) {
			squashHeight = 2;
		}
		else if (this.position.nbTokens() > 4) {
			squashHeight = 4;
		}

		if (this.height + 1 == this.position.nbTokens()) {
			this.setLocation(4, 21 - squashHeight*(this.getTokHeight() - 1));
		}
		else {
			this.setLocation(4, 29 - squashHeight*this.getTokHeight());
		}
		
		g2d.setColor(this.getColor().darker());
		g2d.fillRect(0, 7, 41, 7);
		g2d.setColor(this.getColor().darker().darker());
		g2d.drawRect(0, 7, 41, 7);
		
		g2d.setColor(this.getColor().darker());
		g2d.fillOval(0, 7, 41, 11);
		g2d.setColor(this.getColor().darker().darker());
		g2d.drawArc(0, 7, 41, 11, 180, 180);
		
		g2d.setColor(this.getColor().darker());
		g2d.fillOval(0, 0, 41, 11);
		g2d.setColor(this.getColor().darker().darker());
		g2d.drawOval(0, 0, 41, 11);
		
		if (this.isActive) {
			// If the user can select this token, draw a white cross on its side
			g2d.setColor(Color.WHITE);
			g2d.drawLine(12, 12, 29, 17);
			g2d.drawLine(12, 17, 29, 12);
		}
	}
	
	// Required methods for MouseListener
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
    	if (this.isActive) {
    		// Since isActive is only ever set by human players, we can do this
    		((HumanPlayer) this.player).setChosenToken(this);
    	}
    }
}

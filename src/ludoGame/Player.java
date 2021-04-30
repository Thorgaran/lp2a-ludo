package ludoGame;

import java.awt.Color;
import java.util.*;
import javax.swing.*;

public abstract class Player {
	private PlayerType type;
	
	private Color color;
	private ArrayList<Square> homeSquares;
	private Token[] tokens = new Token[4];
	private boolean hasEaten = false;
	
	private JButton button;
	
	Player(Color color, ArrayList<Square> homes, PlayerType type) {
		this.type = type;
		
		this.color = color;
		this.homeSquares = homes;
		
		for(int i=0; i<4; i++) {
			tokens[i] = new Token(this);
		}
	}
	
	Player(Player oldPlayer, PlayerType newType) {
		// This constructor is used to change the type of a player between games
		this.type = newType;
		
		this.color = oldPlayer.color;
		this.homeSquares = oldPlayer.homeSquares;
		this.tokens = oldPlayer.getTokens();
		
		this.button = oldPlayer.button;
		
		// Change token ownership
		for(Token token: this.tokens) {
			token.setPlayer(this);
		}
	}
	
	public PlayerType getType() {
		return this.type;
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
	
	// Returns an empty home square. Will crash the program if it can't find one
	public Square getEmptyHomeSquare() {
		for (Square square : this.homeSquares) {
			if (square.getTokens().size() == 0) {
				return square;
			}
		}
		System.out.println("No home square empty!");
		System.exit(1);
		return null;
	}
	
	// Returns the corresponding starting square for the player
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
	
	// Returns a String representing the player type, with html coloration information
	public String getColoredType() {
		return Game.dyeText(this.type.toString(), this.color.darker());
	}
	
	// Takes a list of valid tokens and returns one of them (the one that the player wants to move)
	abstract protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens);
	
	// Handles the full logic for a player turn for the current value of the dice
	public void turn(boolean showBoard, int diceResult) {
		HashMap<Token, Square> playableTokens = new HashMap<Token, Square>();
		
		for (Token t: this.getTokens()) { // Check your valid moves
			Square destSquare = t.moveForward(diceResult);
			
			if (destSquare != null) {
				playableTokens.put(t, destSquare);
			}
		}
		
		if (playableTokens.isEmpty()) {
			Game.setInfoText(showBoard, this.getColoredType() + " cannot play and has to pass");
		}
		else {
			Game.setInfoText(showBoard, this.getColoredType() + " must select a token");
		}
		Token chosenToken = this.chooseToken(showBoard, playableTokens);
		
		if (chosenToken != null) {
			Square startSquare = chosenToken.getPosition();
			Square destSquare = playableTokens.get(chosenToken);
			
			if (destSquare.isEatable(chosenToken)) {
				while (destSquare.nbTokens() > 0) {
					Token eatenToken = destSquare.getTokens().get(0);
					Square homeSquare = eatenToken.getPlayer().getEmptyHomeSquare();
					
					eatenToken.move(homeSquare);
					if (showBoard) {
						homeSquare.repaint();	
					}

					eatenToken.setDistance(0);
				}
				
				// If the player is eating for the first time, remove the fork barrier
				if (!this.hasEaten) {
					this.hasEaten = true;
					
					if (showBoard) {
						Square forkSquare = this.homeSquares.get(0);
						for(int i=0; i<51; i++) {
							forkSquare = forkSquare.getNextSquare();
						}
						
						((ForkSquare) forkSquare).unlockGoalRow();
						forkSquare.repaint();
					}
				}
			}
			
			// This needs to be checked before moving the token
			boolean movingBlock = chosenToken.isBlockBase();
			
			chosenToken.move(destSquare);
			chosenToken.commitDistance();
			
			// If the token is a block base, move the token above it too
			if (movingBlock) {
				Token topToken = startSquare.getTokens().get(startSquare.nbTokens() - 1);
				topToken.move(destSquare);
				topToken.setDistance(chosenToken.getDistance());
			}
			
			if (showBoard) {
				// Repaint start and destination squares
				startSquare.repaint();
				destSquare.repaint();
			}
		}
	}
	
	// Returns true if the player reached the goal with its 4 tokens
	public boolean hasWon() {
		boolean hasWon = true;
		for(Token t: this.tokens) {
			if (t.getPosition().getType() != SquareType.Goal) {
				hasWon = false;
			}
		}
		
		return hasWon;
	}
	
	// Resets the player to its pre-game state
	public void reset(boolean showBoard) {
		// Move tokens back to their homes
		for(Token token: this.getTokens()) {
			if (token.getPosition().getType() != SquareType.Home) {
				token.move(this.getEmptyHomeSquare());
				token.setDistance(0);
			}
		}
		
		// Relock the goal row
		this.hasEaten = false;
		
		if (showBoard) {
			Square forkSquare = this.homeSquares.get(0);
			for(int i=0; i<51; i++) {
				forkSquare = forkSquare.getNextSquare();
			}
			
			((ForkSquare) forkSquare).lockGoalRow();
		}
	}
}

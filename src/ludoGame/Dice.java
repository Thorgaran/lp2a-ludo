package ludoGame;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;
import java.awt.*;

public class Dice {
	private int number;
	
	private Icon[] diceFacesImg = {
			new ImageIcon("img/dice_1.png"),
			new ImageIcon("img/dice_2.png"),
			new ImageIcon("img/dice_3.png"),
			new ImageIcon("img/dice_4.png"),
			new ImageIcon("img/dice_5.png"),
			new ImageIcon("img/dice_6.png")
	};
	
	// This hashmap contains 4 JLabels used to display the dices at each player's homes
	private HashMap<Color, JLabel> dispDices = new HashMap<Color, JLabel>();
	
	// This hashmap contains the JLabels once they display medals
	private HashMap<Color, JLabel> dispMedals = new HashMap<Color, JLabel>();
	
	Dice(Board board, Collection<Player> players) {
		this.number = this.roll();
		
		for(Player player: players) {
			JLabel dispDice = new JLabel();
			dispDice.setVisible(false);
			
			// Get the coordinates of the square in the middle of the four homes
			int row = 0;
			int col = 0;
			for(Square home: player.getHomeSquares()) {
				row += home.getRow();
				col += home.getCol();
			}
			row = row / 4;
			col = col / 4;
			
			GridBagConstraints c = new GridBagConstraints();
	        c.fill = GridBagConstraints.HORIZONTAL;
	        dispDice.setPreferredSize(new Dimension(50, 50));
	        c.gridx = col;
	        c.gridy = row;
	        c.gridheight = 1;
	        c.gridwidth = 1;
			
	        board.add(dispDice, c);
			this.dispDices.put(player.getColor(), dispDice);
		}
	}
	
	// Give a random value to the dice
	public int roll() {
		// Get a random int between 1 and 6
		this.number = ThreadLocalRandom.current().nextInt(1, 7);
		
		return this.number;
	}
	
	// Hide all visible dices among the 4 JLabels at the players' homes
	public void hideDices(boolean showBoard) {
		if (showBoard) {			
			// Hide the currently visible dices
			for(JLabel dispDice: this.dispDices.values()) {
				if (dispDice.isVisible()) {
					dispDice.setVisible(false);
				}
			}
		}
	}
	
	// Show the current dice value to the user and eventually show a dice roll animation
	public void dispFace(boolean showBoard, Player player, boolean showAnimation) {
		if (showBoard) {
			Color playerColor = player.getColor();
			
			JLabel dispDice = this.dispDices.get(playerColor);
			dispDice.setVisible(true);
			
			int savedRoll = this.number;
			
			if (showAnimation) {
				Game.setInfoText(showBoard, player.getColoredType() + " is throwing the dice");
				
				double sleepDelta = 1.1;
				do {
					// Display temporary dice face
					this.roll();
					dispDice.setIcon(diceFacesImg[this.number-1]);
					
					Game.sleep(showBoard, 75 + (int) sleepDelta);
					
					// Increase the waiting time slowly at first, then faster and faster
					sleepDelta = Math.pow(sleepDelta, 1.4);
				} while (sleepDelta < 1000);
			}
			
			// Show new dice actual value
			dispDice.setIcon(diceFacesImg[savedRoll-1]);
		}
	}
	
	// To optimize the medal display, we use the dice JLabel that won't be used anymore to display the medal
	// Displayed medal depends on the ranking
	public void changeToMedal(Color playerColor, int ranking) {
		JLabel medal = this.dispDices.get(playerColor);
		
		// Remove the medal after storing it away
		this.dispMedals.put(playerColor, this.dispDices.get(playerColor));
		this.dispDices.remove(playerColor);
		
		switch (ranking) {
			case 1: medal.setIcon(new ImageIcon("img/gold_medal.png"));
				break;
			case 2: medal.setIcon(new ImageIcon("img/silver_medal.png"));
				break;
			case 3: medal.setIcon(new ImageIcon("img/bronze_medal.png"));
				break;
			default: System.out.println("Unavailable ranking");
		}

		medal.setVisible(true);
		medal.repaint();
	}
	
	// Reset the dice to a pre-game state
	public void reset() {
		// Put the medals back in diceDisplay
		this.dispDices.putAll(this.dispMedals);
		this.dispMedals.clear();
		
		// Reset the dispDices to their original state without image
		for(JLabel dispDice: this.dispDices.values()) {
			dispDice.setIcon(null);
			dispDice.setVisible(false);
		}
	}
}

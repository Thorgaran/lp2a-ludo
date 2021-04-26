package ludoGame;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class ResultDialog extends JDialog {
	JButton continueButton;
	ArrayList<JLabel> resultLabels = new ArrayList<JLabel>();
	
	ResultDialog(JFrame window) {
		super(window, "AI results", false);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(new GridBagLayout());
		
		// Add result labels
		this.addResultLabel(1, " ");
		this.addResultLabel(2, " ");
		this.addResultLabel(4, " ");
		this.addResultLabel(5, " ");
		this.addResultLabel(0, " ");
		this.addResultLabel(3, "Please wait (0 / " + Game.NB_AI_GAMES + " games done)");
		
		// Add continue button
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JButton continueButton = new JButton("Continue");
		this.continueButton = continueButton;
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	    		dispose();
	    	}
	    });
		
		c.gridx = 0;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(continueButton, c);
		
		this.setSize(new Dimension(600, 230));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		continueButton.setVisible(false);
		
		this.setVisible(true);
	}
	
	private void addResultLabel(int row, String text) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel resultLabel = new JLabel(text);
		resultLabels.add(resultLabel);
		
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setFont(resultLabel.getFont().deriveFont(16.0f));
		
		c.gridx = 0;
		c.gridy = row;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(resultLabel, c);
	}
	
	public void setGamesDone(int gamesDone) {
		this.resultLabels.get(5).setText("Please wait (" + gamesDone 
			+ " / " + Game.NB_AI_GAMES + " games done)");
	}
	
	public void showResults(Game game, HashMap<Color, Integer> playerWins, int nbOfGames) {
		this.continueButton.setVisible(true);
		
		int comparedAIwins = playerWins.get(Game.SW_COLOR) + playerWins.get(Game.NE_COLOR);
		
		// Specific order to be displayed
		Color[] aiColors = {Game.SW_COLOR, Game.NE_COLOR, Game.NW_COLOR, Game.SE_COLOR};
		
		int i = 0;
		for(Color color: aiColors) {
			Player player = game.getPlayer(color);
			int nbOfWins = playerWins.get(color);
			
			// Player type
			StringBuffer resultText = new StringBuffer("<html>" + player.getColoredType() + ": ");
			// Number of wins
			resultText.append(nbOfWins + " wins ");
			
			// If we are currently displaying the two AIs we're interested in
			if (i < 2) {
				// Winrate
				resultText.append("- " + String.format("%.2f", nbOfWins*100 / (float) comparedAIwins) + "% winrate ");
			}
			
			// % of wins
			resultText.append("(" + String.format("%.2f", nbOfWins*100 / (float) nbOfGames) + "% of total)");
			
			this.resultLabels.get(i).setText(resultText.toString());
			
			i++;
		}
		
		this.resultLabels.get(4).setText("Outcome of " + nbOfGames + " games:");
		this.resultLabels.get(5).setText("----------");
	}
}

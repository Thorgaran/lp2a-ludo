package ludoGame;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class PlayerTypeDialog extends JDialog {
	private int nbLines = 0;
	
	private boolean startGame = false;
	
	PlayerTypeDialog(JFrame window, Color[] playerColors, boolean removeHumans) {
		super(window, "Player selection", true);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new GridBagLayout());
		
		for(Color color: playerColors) {
			this.addPlayerLine(color, removeHumans);
		}
		
		// Add start button
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton startButton = (JButton)e.getSource();
	    		PlayerTypeDialog playerTypeDialog = (PlayerTypeDialog) SwingUtilities.getWindowAncestor(startButton);
	    		
	    		playerTypeDialog.setStartGame();
	    		dispose();
	    	}
	    });
		
		c.gridx = 0;
		c.gridy = this.nbLines;
		c.gridheight = 1;
		c.gridwidth = 2;
		this.getContentPane().add(startButton, c);
		
		this.setSize(new Dimension(180, 180));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
	
	// Add a line displaying a color and a player type selection drop-down list
	private void addPlayerLine(Color color, boolean removeHumans) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// Add color square
		JPanel colorSquare = new JPanel();
		
		colorSquare.setPreferredSize(new Dimension(20, 20));
		colorSquare.setBackground(color);
		colorSquare.setBorder(BorderFactory.createLineBorder(color.darker()));
		c.gridx = 0;
		c.gridy = this.nbLines;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(colorSquare, c);
		
		// Add drop-down list
		ArrayList<String> typeList = new ArrayList<String>(Arrays.asList(PlayerType.getTypeList()));
		if (removeHumans) {
			typeList.remove(typeList.size() - 1);
		}
		
		JComboBox<String> typeListCB = new JComboBox<String>(typeList.toArray(new String[0]));
		typeListCB.setSelectedIndex(Game.getPlayerType(color).toIndex());
		typeListCB.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		// We know this event can only be called by a comboBox so this cast is safe, thus we can suppress the warning
	    		@SuppressWarnings("unchecked")
				JComboBox<String> typeListCB = (JComboBox<String>)e.getSource();

	    		// Why can we use color here? I don't think Java supports closures...
	    		Game.setPlayerType(color, PlayerType.indexToType(typeListCB.getSelectedIndex()));
	    	}
	    });
		
		c.gridx = 1;
		c.gridy = this.nbLines;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(typeListCB, c);
		
		this.nbLines++;
	}
	
	public void setStartGame() {
		this.startGame = true;
	}
	
	public boolean isGameStarting() {
		return this.startGame;
	}
}

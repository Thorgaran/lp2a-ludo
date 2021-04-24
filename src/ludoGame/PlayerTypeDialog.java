package ludoGame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class PlayerTypeDialog extends JDialog {
	private int nbLines = 0;
	
	private boolean startGame = false;
	
	PlayerTypeDialog(JFrame window, Color[] playerColors) {
		super(window, "Player selection", true);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new GridBagLayout());
		
		for(Color color: playerColors) {
			this.addPlayerLine(color);
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
		
		//this.pack();
		this.setSize(new Dimension(180, 180));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
	
	private void addPlayerLine(Color color) {
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
		JComboBox<String> typeList = new JComboBox<String>(PlayerType.getTypeList());
		typeList.setSelectedIndex(0);
		typeList.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		// We know this event can only be called by a comboBox so this cast is safe, thus we can suppress the warning
	    		@SuppressWarnings("unchecked")
				JComboBox<String> typeList = (JComboBox<String>)e.getSource();
	    		
	    		// Dirty way to get the type list grid row
	    		int typeListRow = typeList.getLocation().y / typeList.getHeight();
	    		Game.setPlayerTypeAt(typeListRow, PlayerType.indexToType(typeList.getSelectedIndex()));
	    	}
	    });
		
		c.gridx = 1;
		c.gridy = this.nbLines;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(typeList, c);
		
		this.nbLines++;
	}
	
	public void setStartGame() {
		this.startGame = true;
	}
	
	public boolean isGameStarting() {
		return this.startGame;
	}
}

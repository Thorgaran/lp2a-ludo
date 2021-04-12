package ludoGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel {
	static boolean skipped;
	
	Board() {
		this.setLayout(new GridBagLayout());
	}
	
	public void addSquare(Square square) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		square.setPreferredSize(new Dimension(50, 50));
		square.setBorder(BorderFactory.createLineBorder(square.getColor().darker()));
		c.gridx = square.getCol();
		c.gridy = square.getRow();
		c.gridheight = 1;
		c.gridwidth = 1;
		this.add(square, c);
	}
	
	// Adds a skip button at the given coordinates linked to the skipped variable
	public JButton addSkip(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton skip = new JButton(new ImageIcon("skip.png"));
		skip.setPreferredSize(new Dimension(50, 50));
		skip.setBorder(BorderFactory.createLineBorder(Color.black));
		c.gridx = x;
		c.gridy = y;
		c.gridheight = 1;
		c.gridwidth = 1;
		
		skip.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Board.skipped = true;
	    	}
	    });
		
		skip.setVisible(false);
		this.add(skip, c);
		return skip;
	}
}

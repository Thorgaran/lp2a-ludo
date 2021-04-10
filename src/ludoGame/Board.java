package ludoGame;

import javax.swing.*;
import java.awt.*;

public class Board extends JFrame {
	Board() {
		super("lp2a-ludo");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.getContentPane().setLayout(new GridBagLayout());
	}
	
	public void addSquare(int row, int col, int height, int width) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel square = new JLabel("Square");
		square.setBorder(BorderFactory.createLineBorder(Color.black));
		c.gridx = row;
		c.gridy = col;
		c.gridheight = height;
		c.gridwidth = width;
		this.getContentPane().add(square, c);
	}
	
	public void build() {
		this.pack();
		this.setVisible(true);
	}
}

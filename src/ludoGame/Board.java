package ludoGame;

import javax.swing.*;
import java.awt.*;

public class Board extends JFrame {
	Board() {
		super("lp2a-ludo");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.getContentPane().setLayout(new GridBagLayout());
	}
	
	public DisplayedSquare addSquare(Square square) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		DisplayedSquare dispSquare = new DisplayedSquare(square);
		dispSquare.setPreferredSize(new Dimension(50, 50));
		dispSquare.setBorder(BorderFactory.createLineBorder(Color.black));
		c.gridx = square.getCol();
		c.gridy = square.getRow();
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(dispSquare, c);
		
		return dispSquare;
	}
	
	public void build() {
		this.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
}

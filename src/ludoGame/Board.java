package ludoGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class Board extends JPanel {
	public static int SQUARE_SIZE = 50;
	
	static boolean skipped;
	private Image backgroundImage;
	
	Board() {
		Image toResize = null;
		int background = ThreadLocalRandom.current().nextInt(1, 3);
		String chosenBG;
		switch (background) {
			case 1: chosenBG = "img/background.png";
			break;
			default: chosenBG = "img/background2.png";
		}
		try {
			toResize = ImageIO.read(new File(chosenBG));
		} catch (IOException e) {
			e.printStackTrace();
		    System.exit(1);
		}
		this.backgroundImage = toResize.getScaledInstance(SQUARE_SIZE*15, SQUARE_SIZE*15, Image.SCALE_SMOOTH);
		this.setLayout(new GridBagLayout());
	}
	
	// Add square to be displayed on the board grid
	public void addSquare(Square square) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
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
		JButton skip = new JButton(new ImageIcon("img/skip.png"));
		skip.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
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
	
	public void paintComponent(Graphics g) {
	    // Propagate painting chain (see https://www.oracle.com/java/technologies/painting.html)
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    
	    g2d.drawImage(backgroundImage,0,0,this); 
	}
}

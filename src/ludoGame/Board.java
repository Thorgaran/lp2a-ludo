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
	
	public void diceDisplay(int roll) throws InterruptedException {
		Icon[] diceFacesImg = {
			new ImageIcon("dice_1.png"),
			new ImageIcon("dice_2.png"),
			new ImageIcon("dice_3.png"),
			new ImageIcon("dice_4.png"),
			new ImageIcon("dice_5.png"),
			new ImageIcon("dice_6.png")
		};
		JLabel dice = new JLabel();
		dice.setIcon(diceFacesImg[0]);
		dice.setVisible(false);
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        dice.setPreferredSize(new Dimension(50, 50));
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        dice.setVisible(true);
        this.getContentPane().add(dice, c);	
        this.repaint();
        Thread.sleep(500);
        dice.setIcon(diceFacesImg[2]);
        dice.repaint();
        Thread.sleep(500);
        dice.setIcon(diceFacesImg[4]);
        dice.repaint();
        Thread.sleep(500);
        dice.setIcon(diceFacesImg[roll-1]);
        dice.repaint();
     }
}

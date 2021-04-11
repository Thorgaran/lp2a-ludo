package ludoGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JFrame {
	
	private static boolean started;
	
	Board() {
		super("lp2a-ludo");
		
		Board.started=false;
		
		this.setSize(500, 500);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    JButton start = new JButton("Start game");
	    start.setBounds(this.getWidth()/2-75, this.getHeight()/3-50, 150, 50);
	    JButton exit = new JButton ("Exit");
	    exit.setBounds(this.getWidth()/2-75, this.getHeight()/3+50, 150, 50);
	    
	    exit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(0);
	    	}
	    });
	    
	    start.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Board.started = true;
	    	}
	    });
	    
	    this.setLayout(null);
	    this.add(start);
	    this.add(exit);
	    
	    this.setVisible(true);
	    
	    while (Board.started== false) {
	    	try {
	    		Thread.sleep(100);
	    	} catch (InterruptedException e ) {
	    		e.printStackTrace();
	    		System.exit(1);
	    	}
	    }
	    
	    this.setVisible(false);
		
		this.getContentPane().removeAll();
		
		this.getContentPane().setLayout(new GridBagLayout());
	}
	
	public void addSquare(Square square) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		square.setPreferredSize(new Dimension(50, 50));
		square.setBorder(BorderFactory.createLineBorder(Color.black));
		c.gridx = square.getCol();
		c.gridy = square.getRow();
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(square, c);
	}
	
	public void build() {
		this.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
	
	public void diceDisplay(int roll) {
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
       try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
		System.exit(1);
	}
        dice.setIcon(diceFacesImg[2]);
        dice.repaint();
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
        dice.setIcon(diceFacesImg[4]);
        dice.repaint();
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
        dice.setIcon(diceFacesImg[roll-1]);
        dice.repaint();
     }
}

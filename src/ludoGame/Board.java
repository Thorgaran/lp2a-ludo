package ludoGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JFrame {
	
	private static boolean started;
	static boolean skipped;
	
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
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
	    
	    this.setVisible(true);
	    
	    // Wait for the user to click "Start game"
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
		square.setBorder(BorderFactory.createLineBorder(square.getColor().darker()));
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
	
	//adds a skip button at the given coordinates linked to the skipped variable
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
		this.getContentPane().add(skip, c);
		return skip;
	}
}

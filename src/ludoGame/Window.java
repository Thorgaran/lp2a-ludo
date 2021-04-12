package ludoGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
	private static boolean started = false;
	
	Window () {
		super("lp2a-ludo");
		
		this.setSize(500, 500);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
		JButton start = new JButton("Start game");
		start.setBounds(this.getWidth()/2 - 75, this.getHeight()/3 - 50, 150, 50);
		JButton exit = new JButton ("Exit");
		exit.setBounds(this.getWidth()/2 - 75, this.getHeight()/3 + 50, 150, 50);
	    
	    exit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(0);
	    	}
	    });
	    
	    start.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Window.started = true;
	    	}
	    });
	    
	    this.getContentPane().setLayout(null);
	    this.getContentPane().add(start);
	    this.getContentPane().add(exit);
		
	    // Center window
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	    
	    // Wait for the user to click "Start game"
	    while (!Window.started) {
	    	try {
	    		Thread.sleep(100);
	    	} catch (InterruptedException e ) {
	    		e.printStackTrace();
	    		System.exit(1);
	    	}
	    }
	}
	
	public Board setupBoard() {
		this.getContentPane().removeAll();
		
		Board board = new Board();
		
		this.setContentPane(board);
				
		return board;
	}
	
	public void rebuild() {
		this.pack();
		
		// Center window
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
	}
}

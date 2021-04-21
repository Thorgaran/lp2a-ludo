package ludoGame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
	private static MenuState started = MenuState.selection;
	static int playerCount;
	
	Window () {
		super("lp2a-ludo");
		
		this.setSize(500, 500);
		this.setResizable(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
		JButton start = new JButton("Start game");
		start.setBounds(this.getWidth()/2 - 75, this.getHeight()/3 , 150, 50);
		JButton exit = new JButton ("Exit");
		exit.setBounds(this.getWidth()/2 - 75, this.getHeight()/3 + 200, 150, 50);
		JButton rules = new JButton ("Rules");
		rules.setBounds(this.getWidth()/2 - 75, this.getHeight()/3 + 100, 150, 50);
	    
	    exit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(0);
	    	}
	    });
	    
	    start.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Window.started = MenuState.game;
	    	}
	    });
	    
	    rules.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Window.started = MenuState.rules;
	    	}
	    });
	    
	    this.getContentPane().setLayout(null);
	    this.getContentPane().add(start);
	    this.getContentPane().add(exit);
	    this.getContentPane().add(rules);
	    
	    //create, configure and add a slider and its label to choose the number of players
	    JSlider count = new JSlider(0,4,0);
	    count.setBounds(this.getWidth()/2 - 75, this.getHeight()/3 - 80, 150, 50);
	    count.setMajorTickSpacing(1);
	    count.setPaintTicks(true);
	    count.setPaintLabels(true);
	    count.setPaintTrack(true);
	    
	    Window.playerCount=count.getValue();
	    count.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Window.playerCount=count.getValue();
			}
	    });
	    
	    this.getContentPane().add(count);
	    
	    JLabel countText = new JLabel("Number of human players:");
	    countText.setBounds(this.getWidth()/2 - 75, this.getHeight()/3 - 150, 300, 100);
	    this.getContentPane().add(countText);
		
	    // Center window
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	    
		// Wait for the player to start the game or close the window
		while (Window.started != MenuState.game) {
		    // Wait for the user to click "Start game" or "rules"
		    while (Window.started == MenuState.selection) {
		    	try {
		    		Thread.sleep(100);
		    	} catch (InterruptedException e ) {
		    		e.printStackTrace();
		    		System.exit(1);
		    	}
		    }
		    // Display rules if the player clicks the rules button
		    if (Window.started == MenuState.rules) {
		    	exit.setVisible(false);
		    	start.setVisible(false);
		    	rules.setVisible(false);
		    	count.setVisible(false);
		    	countText.setVisible(false);
		    	
		    	this.dispRules();
		    	
		    	exit.setVisible(true);
		    	start.setVisible(true);
		    	rules.setVisible(true);
		    	count.setVisible(true);
		    	countText.setVisible(true);
		    	this.repaint();
		    }
		}
		// Then the game board is initialized in the main method and the game starts
	}

	public Board setupBoard() {
		this.getContentPane().removeAll();
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		JLabel infoText = new JLabel(" ", SwingConstants.CENTER);
		infoText.setAlignmentX(CENTER_ALIGNMENT);
		infoText.setFont(infoText.getFont().deriveFont(16.0f));
		this.getContentPane().add(infoText);
		
		Board board = new Board();
		board.setMinimumSize(new Dimension(Board.SQUARE_SIZE*15, Board.SQUARE_SIZE*15));
		this.getContentPane().add(board);
		
		return board;
	}
	
	public void rebuild() {
		this.pack();
		
		// Center window
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
	}
	
	private void dispRules() {
		JButton back = new JButton("Back");
		back.setBounds(this.getWidth() -125, this.getHeight() - 75, 100, 25);
		
		back.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Window.started = MenuState.selection;
	    	}
	    });
		
		JLabel rulesText = new JLabel();
		//we write the rules from the project pdf
		rulesText.setText("<html>RULES:<br>-Each player chooses one of the 4 colors (green, yellow, red or blue)"
				+ " and places the 4 pieces of that color in the corresponding block.<br>-"
				+ "The players start the run of their pieces from the starting square.<br>-"
				+"Players take turns in a clockwise order; highest throw of the dice starts.<br>-"
				+ "Each throw, the player decides which piece to move by clicking it. A piece simply moves in a "
				+ "clockwise direction around the track given by the number thrown. If no piece can legally "
				+ "move according to the number thrown, play passes to the next player.<br>-"
				+"A player must throw a 6 to move a piece from the starting block onto the first square on "
				+ "the track. The piece moves 6 squares around the circuit beginning with the appropriately "
				+ "colored start square (and the player then has another turn).<br>-"
				+"Colored squares and the star ones are defined as safe zones that protect the piece from "
				+ "being sent to the starting block.<br>-"
				+"If a piece lands on a piece of a different color, the piece jumped upon is returned to its "
				+ "starting block except in case of safe zones.<br>-"
				+ "If a piece lands upon a piece of the same color, this forms a block. This block cannot be "
				+ "passed or landed on by any opposing piece, but can be eaten by an other block"
				+ ".<br> <br>More details about the rules can be found in this video :<br>"
				+"Mr.Animate: Ludo Board Game Rules & Instructions | Learn How To Play Ludo Game");
		rulesText.setBounds(40,10,400,400);
		rulesText.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.getContentPane().add(back);
		this.getContentPane().add(rulesText);
		this.repaint();
		//wait for the player to read the rules and go back
    	while (Window.started==MenuState.rules) {
	    	try {
	    		Thread.sleep(100);
	    	} catch (InterruptedException e ) {
	    		e.printStackTrace();
	    		System.exit(1);
	    	}
	    }
    	
		this.remove(back);
		this.remove(rulesText);
	}
}

package ludoGame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
	private static MenuState menuState = MenuState.None;
	private Board board;
	private JPanel menuPanel;
	private JPanel playPanel;
	
	Window () {
		super("lp2a-ludo");
		
		this.setResizable(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    this.createMenu();
	    this.createBoard();
	    
	    /*//create, configure and add a slider and its label to choose the number of players
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
	    
	    this.getContentPane().add(count);*/
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public JLabel getInfoText() {
		// We know the first component is a JLabel, thus we can cast it
		return (JLabel) this.playPanel.getComponent(0);
	}
	
	public void menuLoop() {
		// Display menu
		
		this.setVisible(false);
		
		this.setContentPane(this.menuPanel);
		this.setSize(300, 400);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
		
		// Start menu loop
		boolean startGame = false;
		while (!startGame) {
			switch (Window.menuState) {
			case Play:
				// JDialog playerTypeDialog = new JDialog(this, "Player selection", true);
				startGame = true;
				Window.menuState = MenuState.None;
				break;
			
			case Rules:
				this.dispRules();
				Window.menuState = MenuState.None;
				break;
			
			case Exit:
				System.exit(0);
				
			case None:
			default:
				// Read user input every 100ms
				try {
		    		Thread.sleep(100);
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    		System.exit(1);
		    	}
			}
		}
		
		this.setVisible(false);
		
		this.setContentPane(this.playPanel);
		this.pack();

	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
	
	public void createMenu() {
		JPanel menuPanel = new JPanel();
		
		menuPanel.setSize(300, 500);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
		
	    // Place the menu buttons
	    menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		
	    this.addMenuButton(menuPanel, "Play", MenuState.Play);
	    
	    menuPanel.add(Box.createVerticalGlue());
		
		this.addMenuButton(menuPanel, "Rules", MenuState.Rules);
		
		menuPanel.add(Box.createVerticalGlue());
		
	    this.addMenuButton(menuPanel, "Exit", MenuState.Exit);
		
	    menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
	    
	    this.menuPanel = menuPanel;
	}
	
	private void addMenuButton(JPanel menuPanel, String buttonLabel, MenuState resultingMenuState) {		
		JButton menuButton = new JButton(buttonLabel);
		menuButton.setAlignmentX(CENTER_ALIGNMENT);
		
		// For the buttons, all 3 sizes constraints need to be set
	    // See https://stackoverflow.com/questions/21734374/setminimumsize-not-working-for-jbutton
		Dimension buttonDim = new Dimension(150, 50);
		menuButton.setMinimumSize(buttonDim);
		menuButton.setPreferredSize(buttonDim);
		menuButton.setMaximumSize(buttonDim);

		menuPanel.add(menuButton);
		
		menuButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Window.menuState = resultingMenuState;
	    	}
	    });
	}
	
	private void createBoard() {
		JPanel playPanel = new JPanel();
		playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.PAGE_AXIS));
		
		JLabel infoText = new JLabel(" ", SwingConstants.CENTER);
		infoText.setAlignmentX(CENTER_ALIGNMENT);
		infoText.setFont(infoText.getFont().deriveFont(16.0f));
		playPanel.add(infoText);
		
		Board board = new Board();
		board.setMinimumSize(new Dimension(Board.SQUARE_SIZE*15, Board.SQUARE_SIZE*15));
		playPanel.add(board);
		
		this.board = board;
		this.playPanel = playPanel;
	}
	
	private void dispRules() {
		JDialog rulesDialog = new JDialog(this, "Rules", true);

		JLabel rulesText = new JLabel();
		//we write the rules from the project pdf
		rulesText.setText("<html><u><b>Rules:</b></u><br><br>"
			+ "  - The starting player is randomly decided.<br><br>"
			+ "  - Players take turns in clockwise order. At the start of each turn, the dice is thrown.<br><br>"
			+ "  - The player then selects a token to move forward according to this value.<br><br>"
			+ "  - The tokens the player can move will be highlighted with a white cross.<br><br>"
			+ "  - The player can click on such tokens to move them forward.<br><br>"
			+ "  - If there are no valid moves, a skip buttons appears, and the player must click on it.<br><br>"
			+ "  - A player must throw a 6 to move a piece out of its home.<br><br>"
			+ "  - Whenever the player throws a 6, the player gets another turn afterwards, unless it's the third 6 in a row.<br><br>"
			+ "  - If a piece lands on a piece of a different color, the piece jumped upon is returned to its home.<br><br>"
			+ "  - If a piece lands upon a piece of the same color, this forms a block.<br><br>"
			+ "  - Whenever the dice has an even value, a full block can be moved by half this value by clicking on its bottom token.<br><br>"
			+ "  - A block can eat another block. Any pile of tokens cannot be passed, but they can be landed upon.<br><br>"
			+ "  - A token cannot move if it has a token above it, unless it's part of a block.<br><br>"
			+ "  - The colored squares are safe squares, meaning no tokens can be eaten on them. The tokens simply stack.<br><br>"
			+ "  - Once a token made a full lap of the track, it can enter the player's goal row, if this player has already eaten once.<br><br>"
			+ "  - Until the player eats a token, a gray barrier is drawn in front of its goal row to show that it can't be entered.<br><br>"
			+ "  - A token can reach the end of the goal row by perfectly landing on the last square.<br><br>"
			+ "  - The first player with four tokens at the end of the goal row wins.<br><br>"
		);
		rulesText.setHorizontalAlignment(SwingConstants.CENTER);
		rulesText.setFont(rulesText.getFont().deriveFont(16.0f));
		
		rulesDialog.getContentPane().add(rulesText);
		rulesDialog.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		rulesDialog.setLocation(dim.width/2 - rulesDialog.getWidth()/2, dim.height/2 - rulesDialog.getHeight()/2);
		
		rulesDialog.setVisible(true);
	}
}

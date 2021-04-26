package ludoGame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

@SuppressWarnings("serial")
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
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	// Returns the info text JLabel
	public JLabel getInfoText() {
		// We know the first component is a JLabel, thus we can cast it
		return (JLabel) this.playPanel.getComponent(0);
	}
	
	// Main loop of the program, will only be exited once the user quits the game
	public void menuLoop(Game game) {
		// Display menu
		this.dispMenu();
		
		// Start menu loop
		while (true) {
			switch (Window.menuState) {
			case Play:
				PlayerTypeDialog playerTypeDialog = new PlayerTypeDialog(this, Game.PLAYER_COLORS, false);
				
				if (playerTypeDialog.isGameStarting()) {
					this.dispBoard();
					
					game.updatePlayerTypes();
					
					game.play(true);
					
					this.dispMenu();
				}
				
				Window.menuState = MenuState.None;
				break;
			
			case PlayAI:
				// Set all player types to Random AI to prevent the user from selecting human players
				for(Color color: Game.PLAYER_COLORS) {
					Game.setPlayerType(color, PlayerType.RandomAI);
				}
				
				Color[] aiColors = {Game.SW_COLOR, Game.NE_COLOR};
				PlayerTypeDialog playerTypeDialogAIonly = new PlayerTypeDialog(this, aiColors, true);
				
				if (playerTypeDialogAIonly.isGameStarting()) {
					game.updatePlayerTypes();
					
					ResultDialog resultDialog = new ResultDialog(this);
					
					// Create a hashmap to count player wins
					HashMap<Color, Integer> playerWins = new HashMap<Color, Integer>();
					for(Color color: Game.PLAYER_COLORS) {
						playerWins.put(color, 0);
					}
					
					for(int i=0; i < Game.NB_AI_GAMES; i++) {
						Color winner = game.play(false).get(0);

						playerWins.put(winner, playerWins.get(winner) + 1);
						
						// Show the number of games done to the user
						if ((i + 1) % 500 == 0) {
							resultDialog.setGamesDone(i+1);
						}
					}
					
					resultDialog.showResults(game, playerWins, Game.NB_AI_GAMES);
				}
				
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
				Game.sleep(true, 100);
			}
		}
	}
	
	// Places components to make the menu on a JPanel and returns it
	public void createMenu() {
		JPanel menuPanel = new JPanel();
		
		menuPanel.setSize(300, 500);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
		
	    // Place the menu buttons
	    menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		
	    this.addMenuButton(menuPanel, "Play", MenuState.Play);
	    
	    menuPanel.add(Box.createVerticalGlue());
	    
	    this.addMenuButton(menuPanel, "AI comparison", MenuState.PlayAI);
		
		menuPanel.add(Box.createVerticalGlue());
		
		this.addMenuButton(menuPanel, "Rules", MenuState.Rules);
		
		menuPanel.add(Box.createVerticalGlue());
		
	    this.addMenuButton(menuPanel, "Exit", MenuState.Exit);
		
	    menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
	    
	    this.menuPanel = menuPanel;
	}
	
	// Add single button to the menu
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
	
	// Places components to make the board on a JPanel and returns it
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
	
	// When the window is currently displaying the board, this is used to quickly switch to the menu
	private void dispMenu() {
		this.setVisible(false);
		
		this.setContentPane(this.menuPanel);
		this.setSize(300, 400);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
	
	// When the window is currently displaying the menu, this is used to quickly switch to the board
	private void dispBoard() {
		this.setVisible(false);
		
		this.setContentPane(this.playPanel);
		this.pack();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
	
	// Opens a pop-up window displaying the Ludo rules
	private void dispRules() {
		JDialog rulesDialog = new JDialog(this, "Rules", true);
		rulesDialog.setResizable(false);
		rulesDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

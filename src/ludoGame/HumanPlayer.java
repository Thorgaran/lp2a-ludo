package ludoGame;

import java.awt.Color;
import java.util.*;

public class HumanPlayer extends Player {
	Token chosenToken;
	
	HumanPlayer(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.HumanPlayer);
	}
	
	HumanPlayer(Player oldPlayer) {
		super(oldPlayer, PlayerType.HumanPlayer);
	}
	
	public void setChosenToken(Token token) {
		this.chosenToken = token;
	}
	
	protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens) {
		if (!showBoard) {
			System.out.println("Error! Human player cannot be called with showBoard=false!");
			System.exit(1);
		}
		
		this.chosenToken = null;
		List<Token> tokenList = new ArrayList<>(playableTokens.keySet());
		
		if (tokenList.isEmpty()) {
			Board.skipped = false;
			
			this.getButton().setVisible(true);
			this.getButton().repaint();
			
			while (!Board.skipped) {
		    	Game.sleep(showBoard, 100);
		    }
			
			this.getButton().setVisible(false);
			this.getButton().repaint();
		}
		else {
			for(Token token: tokenList) {
				token.setIsActive(true);
				token.repaint();
			}
		
			while (this.chosenToken == null) {
				Game.sleep(showBoard, 100);
			}
			
			for(Token token: tokenList) {
				token.setIsActive(false);
				token.repaint();
			}
		}
		
		return this.chosenToken;
	}
}

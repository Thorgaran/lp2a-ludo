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
	
	protected Token chooseToken(HashMap<Token, Square> playableTokens) {
		this.chosenToken = null;
		List<Token> tokenList = new ArrayList<>(playableTokens.keySet());
		
		if (tokenList.isEmpty()) {
			Board.skipped = false;
			
			this.getButton().setVisible(true);
			this.getButton().repaint();
			
			while (!Board.skipped) {
		    	try {
		    		Thread.sleep(100);
		    	} catch (InterruptedException e ) {
		    		e.printStackTrace();
		    		System.exit(1);
		    	}
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
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			
			for(Token token: tokenList) {
				token.setIsActive(false);
				token.repaint();
			}
		}
		
		return this.chosenToken;
	}
}

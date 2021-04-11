package ludoGame;

import java.awt.Color;
import java.util.*;

public class HumanPlayer extends Player {
	Token chosenToken;
	
	HumanPlayer(Color color, ArrayList<Square> homes) {
		super(color, homes);
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
			while (Board.skipped== false) {
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
		
		/*int nbOptions = 0;
		for(Token token: tokenList) {
			nbOptions++;
			System.out.print(nbOptions + ": " + (char) (token.getPosition().getCol() + 65) + " " + (token.getPosition().getRow() + 1));
			System.out.println(token.isBlockBase() ? " (block)" : "");
		}
		
		for(int i = nbOptions; i < 4; i++) {
			System.out.println("-");
		}
		
		if (nbOptions == 0) {
			Player.keyboard.nextLine();
		}
		else {
			int chosenOption;
			do {
				chosenOption = Player.keyboard.nextInt();
			} while (chosenOption <= 0 || chosenOption > nbOptions);
			Player.keyboard.nextLine();
			
			chosenToken = tokenList.get(chosenOption - 1);
		}*/
		
		return this.chosenToken;
	}
}

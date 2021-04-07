package ludoGame;

import java.awt.Color;
import java.util.*;

public class HumanPlayer extends Player {
	HumanPlayer(Color color, ArrayList<Square> homes) {
		super(color, homes);
	}
	
	protected Token chooseToken(HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		
		List<Token> tokenList = new ArrayList<>(playableTokens.keySet());
		
		int nbOptions = 0;
		for(Token token: tokenList) {
			nbOptions++;
			System.out.println(nbOptions + ": " + (char) (token.getPosition().getCol() + 65) + " " + (token.getPosition().getRow() + 1));
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
		}
		
		return chosenToken;
	}
}

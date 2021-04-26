package ludoGame;

import java.awt.Color;
import java.util.*;

// This AI eats whenever it can, otherwise it plays randomly
public class AggressiveAI extends Player {
	AggressiveAI(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.AggressiveAI);
	}
	
	AggressiveAI(Player oldPlayer) {
		super(oldPlayer, PlayerType.AggressiveAI);
	}
	
	protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (playableTokens.size() == 1) {
			// If there is a single token, play it (this avoids some useless computations)
			chosenToken = (Token) playableTokens.keySet().toArray()[0];
		}
		else if (!playableTokens.isEmpty()) {
			// Only keep tokens that can eat
			ArrayList<Token> tokensThatCanEat = new ArrayList<Token>();
			
			for(Token token: playableTokens.keySet()) {
				if (playableTokens.get(token).isEatable(token)) {
					tokensThatCanEat.add(token);
				}
			}
			
			Random generator = new Random();
			if (!tokensThatCanEat.isEmpty()) {
				// If there are tokens that can eat, select one of them randomly
				chosenToken = tokensThatCanEat.get(generator.nextInt(tokensThatCanEat.size()));
			}
			else {
				// Otherwise, just pick a token randomly
				Object[] keys = playableTokens.keySet().toArray();
				chosenToken = (Token) keys[generator.nextInt(keys.length)];
			}
		}
		
		Game.sleep(showBoard, 500);
		
		return chosenToken;
	}
}

package ludoGame;

import java.awt.Color;
import java.util.*;

// This AI first tries to eat, otherwise it tries to get a token out of home, 
// otherwise it moves its most advanced token
public class SmartAI extends Player {
	SmartAI(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.SmartAI);
	}
	
	SmartAI(Player oldPlayer) {
		super(oldPlayer, PlayerType.SmartAI);
	}
	
	protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (playableTokens.size() == 1) {
			// If there is a single token, play it (this avoids some useless computations)
			chosenToken = (Token) playableTokens.keySet().toArray()[0];
		}
		else if (!playableTokens.isEmpty()) {
			ArrayList<Token> candidates = new ArrayList<Token>();
			
			// Only keep tokens that can eat
			for(Token token: playableTokens.keySet()) {
				if (playableTokens.get(token).isEatable(token)) {
					candidates.add(token);
				}
			}
			
			if (candidates.isEmpty()) {
				// Only keep tokens still at home
				for(Token token: playableTokens.keySet()) {
					if (token.getPosition().getType() == SquareType.Home) {
						candidates.add(token);
					}
				}
				
				if (candidates.isEmpty()) {
					// Sort tokens by distance from greatest to lowest
					Token[] sortedTokens = playableTokens.keySet().toArray(new Token[0]);
					Arrays.sort(sortedTokens, Comparator.comparingInt(tok -> ((Token) tok)
							.getDistance()).reversed());
					
					// Keep only tokens with highest distance
					int maxDistance = sortedTokens[0].getDistance();
					int i = 0;
					while (i < sortedTokens.length && sortedTokens[i].getDistance() == maxDistance) {
						candidates.add(sortedTokens[i]);
						i++;
					}
				}
			}
			
			// Pick a token randomly among the valid candidates
			Random generator = new Random();
			chosenToken = (Token) candidates.get(generator.nextInt(candidates.size()));
		}
		
		Game.sleep(showBoard, 500);
		
		return chosenToken;
	}
}

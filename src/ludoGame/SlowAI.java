package ludoGame;

import java.awt.Color;
import java.util.*;

// This AI always moves the most advanced token
public class SlowAI extends Player {
	SlowAI(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.SlowAI);
	}
	
	SlowAI(Player oldPlayer) {
		super(oldPlayer, PlayerType.SlowAI);
	}
	
	protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (playableTokens.size() == 1) {
			// If there is a single token, play it (this avoids some useless computations)
			chosenToken = (Token) playableTokens.keySet().toArray()[0];
		}
		else if (!playableTokens.isEmpty()) {
			// Sort tokens by distance from lowest to greatest
			Token[] sortedTokens = playableTokens.keySet().toArray(new Token[0]);
			Arrays.sort(sortedTokens, Comparator.comparingInt(tok -> ((Token) tok).getDistance()));
			
			// Keep only tokens with lowest distance
			int minDistance = sortedTokens[0].getDistance();
			ArrayList<Token> tokensWithMinDistance = new ArrayList<Token>();
			int i = 0;
			while (i < sortedTokens.length && sortedTokens[i].getDistance() == minDistance) {
				tokensWithMinDistance.add(sortedTokens[i]);
				i++;
			}
			
			// Randomly select a token among these ones
			Random generator = new Random();
			chosenToken = tokensWithMinDistance.get(generator.nextInt(i));
		}
		
		Game.sleep(showBoard, 500);
		
		return chosenToken;
	}
}

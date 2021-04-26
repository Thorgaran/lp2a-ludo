package ludoGame;

import java.awt.Color;
import java.util.*;

// This AI always moves the most advanced token
public class SpeedrunAI extends Player {
	SpeedrunAI(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.SpeedrunAI);
	}
	
	SpeedrunAI(Player oldPlayer) {
		super(oldPlayer, PlayerType.SpeedrunAI);
	}
	
	protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (playableTokens.size() == 1) {
			// If there is a single token, play it (this avoids some useless computations)
			chosenToken = (Token) playableTokens.keySet().toArray()[0];
		}
		else if (!playableTokens.isEmpty()) {
			// Sort tokens by distance from greatest to lowest
			Token[] sortedTokens = playableTokens.keySet().toArray(new Token[0]);
			Arrays.sort(sortedTokens, Comparator.comparingInt(tok -> ((Token) tok).getDistance()).reversed());
			
			// Keep only tokens with highest distance
			int maxDistance = sortedTokens[0].getDistance();
			ArrayList<Token> tokensWithMaxDistance = new ArrayList<Token>();
			int i = 0;
			while (i < sortedTokens.length && sortedTokens[i].getDistance() == maxDistance) {
				tokensWithMaxDistance.add(sortedTokens[i]);
				i++;
			}
			
			// Randomly select a token among these ones
			Random generator = new Random();
			chosenToken = tokensWithMaxDistance.get(generator.nextInt(i));
		}
		
		Game.sleep(showBoard, 500);
		
		return chosenToken;
	}
}

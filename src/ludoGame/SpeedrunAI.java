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
	
	protected Token chooseToken(HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (!playableTokens.isEmpty()) {
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
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return chosenToken;
	}
}

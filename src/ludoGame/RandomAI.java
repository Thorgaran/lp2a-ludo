package ludoGame;

import java.awt.Color;
import java.util.*;

// This AI decides its move randomly among the available ones
public class RandomAI extends Player {
	RandomAI(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.RandomAI);
	}
	
	RandomAI(Player oldPlayer) {
		super(oldPlayer, PlayerType.RandomAI);
	}
	
	protected Token chooseToken(HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (!playableTokens.isEmpty()) {
			Random generator = new Random();
			Object[] keys = playableTokens.keySet().toArray();
			chosenToken = (Token) keys[generator.nextInt(keys.length)];
		}
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return chosenToken;
	}
}

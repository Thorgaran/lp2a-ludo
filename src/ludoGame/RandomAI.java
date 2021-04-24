package ludoGame;

import java.awt.Color;
import java.util.*;

public class RandomAI extends Player {
	RandomAI(Color color, ArrayList<Square> homes) {
		super(color, homes);
	}
	
	RandomAI(Player oldPlayer) {
		super(oldPlayer);
	}
	
	protected Token chooseToken(HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (!playableTokens.isEmpty()) {
			// This AI decides its move randomly
			Random generator = new Random();
			Object[] keys = playableTokens.keySet().toArray();
			chosenToken = (Token) keys[generator.nextInt(keys.length)];
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return chosenToken;
	}
}

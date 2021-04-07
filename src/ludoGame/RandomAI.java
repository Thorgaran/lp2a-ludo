package ludoGame;

import java.awt.Color;
import java.util.*;

public class RandomAI extends Player {
	RandomAI(Color color, ArrayList<Square> homes) {
		super(color, homes);
	}
	
	protected Token chooseToken(HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (!playableTokens.isEmpty()) {
			// This AI decides its move randomly
			Random generator = new Random();
			Object[] keys = playableTokens.keySet().toArray();
			chosenToken = (Token) keys[generator.nextInt(keys.length)];
		}
		System.out.println("AI is playing...\n-\n-\n-");
		
		Player.keyboard.nextLine();
		
		return chosenToken;
	}
}

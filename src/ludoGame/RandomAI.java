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
	
	protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (playableTokens.size() == 1) {
			// If there is a single token, play it (this avoids some useless computations)
			chosenToken = (Token) playableTokens.keySet().toArray()[0];
		}
		else if (!playableTokens.isEmpty()) {
			Random generator = new Random();
			Object[] keys = playableTokens.keySet().toArray();
			chosenToken = (Token) keys[generator.nextInt(keys.length)];
		}
		
		Game.sleep(showBoard, 500);
		
		return chosenToken;
	}
}

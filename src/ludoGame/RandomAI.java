package ludoGame;

import java.awt.Color;
import java.util.*;

public class RandomAI extends Player {
	private int[] choice= {
			3,5,2,6,
			5,1,3,2,
			6,3,4,1,2,
			4,0,0,3,0,0,
			1,0,0,0,0,
			1,0,0,0,
			6,1,0,0,0,
			6,1,0,0,0,
			4,0,0,1,1,1,
			6,4,0,0,0,
			4,0,0,1,1,0,
			1,1,1,1,1,1,0,
			1,2,2,2,2,2,0,
			1,1,0,0,0,
	};
	RandomAI(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.RandomAI);
	}
	
	RandomAI(Player oldPlayer) {
		super(oldPlayer, PlayerType.RandomAI);
	}
	
	protected Token chooseToken(HashMap<Token, Square> playableTokens) {
		Token chosenToken = null;
		if (!playableTokens.isEmpty()) {
			// This AI decides its move randomly
			Random generator = new Random();
			Object[] keys = playableTokens.keySet().toArray();
			if (Game.turn>=this.choice.length) {
				chosenToken = (Token) keys[generator.nextInt(keys.length)];
			} else {
				chosenToken = (Token) keys[this.choice[Game.turn]];
			}
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return chosenToken;
	}
}

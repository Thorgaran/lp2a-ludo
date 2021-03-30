package ludoGame;

import java.awt.Color;

public class Player {
	private Color color;
	Token[] tokens = new Token[4];
	
	Player(Color color) {
		this.color = color;
		
		for(int i=0; i<4; i++) {
			tokens[i] = new Token();
		}
	}
}

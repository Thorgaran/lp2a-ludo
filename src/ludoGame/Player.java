package ludoGame;

import java.awt.Color;

public class Player {
	private Color color;
	private Square start;
	private Token[] tokens = new Token[4];
	
	Player(Color color, Square start) {
		this.color = color;
		this.start = start;
		
		for(int i=0; i<4; i++) {
			tokens[i] = new Token();
		}
	}
	
	public Square getStartSquare() {
		return this.start;
	}
}

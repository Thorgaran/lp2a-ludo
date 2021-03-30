package ludoGame;

import java.awt.Color;

public class Game {
	Player[] players = new Player[4];
	
	Game() {
		// Create the 4 players
		Color[] playerColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
		
		for(int i=0; i<4; i++) {
			players[i] = new Player(playerColors[i]);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Hello world!");

	}

}

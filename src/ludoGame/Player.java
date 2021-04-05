package ludoGame;

import java.awt.Color;
import java.util.*;

public class Player {
	private Color color;
	private ArrayList<Square> homeSquares;
	private Token[] tokens = new Token[4];
	
	Player(Color color, ArrayList<Square> homes) {
		this.color = color;
		this.homeSquares = homes;
		
		for(int i=0; i<4; i++) {
			tokens[i] = new Token();
		}
	}
	
	public ArrayList<Square> getHomeSquares() {
		return this.homeSquares;
	}
	
	public Square getStartSquare() {
		return this.homeSquares.get(0).getNextSquare();
	}
	
	public Color getColor() {
		return this.color;
	}
}

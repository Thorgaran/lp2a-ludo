package ludoGame;
import java.awt.Color;

public class Square {
	private int index;
	private Token token; //null if empty
	private SquareTypes type;
	private Color colour; //white if blank
	
	public int getIndex() {
		return this.index;
	}
	
	public void setToken(Token temp) {
		this.token = temp;
	}
	public Token getToken() {
		return this.token;
	}
	
	public SquareTypes getType() {
		return this.type;
	}
	
	public Color getColour() {
		return this.colour;
	}
	
	Square (int i, SquareTypes t,Color c){
		this.index = i;
		this.token = null;
		this.type=t;
		this.colour=c;
	}
	Square(){
		System.out.println("Incorrect square initialization!");
	}
}

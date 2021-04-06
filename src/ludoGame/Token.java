package ludoGame;

public class Token {
	private Square position;
	private Player player;
	
	Token() {
		System.out.println("Incorrect token initialization!");
	}
	
	Token(Player p) {
		this.player = p;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Square getPosition() {
		return this.position;
	}
	
	public void setPosition(Square s) {
		this.position = s;
	}
}

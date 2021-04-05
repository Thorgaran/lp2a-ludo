package ludoGame;

public class Token {
	// Square position;
	private Player player;
	
	Token(){
		System.out.println("Incorrect token initialization!");
	}
	Token(Player p){
		this.player=p;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}

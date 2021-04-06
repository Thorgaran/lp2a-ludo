package ludoGame;

public class Token {
	private Square position;
	private Player player;
	private int height;
	
	Token() {
		System.out.println("Incorrect token initialization!");
	}
	
	Token(Player p) {
		this.player = p;
		this.height=0;
		this.position=p.getEmptyHomeSquare();
		this.position.addToken(this);
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
	
	public void setHeight(int h) {
		this.height=h;
	}
	public int getHeight() {
		return this.height;
	}
	
	public boolean isBlocked() {
		
		if (this.position.getTokens().size() > this.height + 1) {
			
			for (int i = this.height + 1; i < this.position.getTokens().size(); i++) {
				if (this.position.getTokens().get(i).getPlayer().getColor() != this.player.getColor()) {
					return true;
				}
			}
			return false;
		
		} else {
			return false;
		}
	
	}
	
	// This function returns the square the token will be landing on, or null if the move is illegal
	public Square moveForward(int movNb) { //tatakae, tatakae
		Square toReturn = this.getPosition();
		
		if (toReturn.getType() == SquareType.Home) { 
			// We check if the token is still trying to get out of its home
			
			if (movNb == 6) {
				toReturn = toReturn.getNextSquare();
			} else {
				return null;
			}
		
		} else if (this.isBlocked()==true) {
			return null;
		
		} else {
			// The token is already in play
			
			for(int i=0; i < movNb; i++) {
				
				if (toReturn.getType() == SquareType.Fork && 
					toReturn.getColor() == this.getPlayer().getColor() 
					&& this.getPlayer().hasEaten()) 
				{
					// Case diverges if we are entering the goal row
					toReturn = ((ForkSquare) toReturn).getGoalRowSquare();
				} else {
					toReturn = toReturn.getNextSquare();
				}
				if (this.position.isBlocking() == true && i!=movNb-1) {
				  	return null;
				}
				if (toReturn.getType() == SquareType.Goal && i < movNb - 1) {
					return null; // The token can't reach the goal unless it gets the exact number of moves to land on it
				}
			
			}
		
		}
		
		return toReturn;
	}
	
	public void move(Square dest) {
		this.setHeight(dest.getTokens().size());
		dest.addToken(this);
		this.position.removeToken(this);
		this.position=dest;
	}
}

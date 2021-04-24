package ludoGame;

import java.util.ArrayList;

public enum PlayerType {
	HumanPlayer,
	RandomAI;
	
	public String toString() {
		switch (this) {
		case HumanPlayer:
			return "Human Player";
		case RandomAI:
			return "Random AI";
		default:
			System.out.println("Invalid player type");
			System.exit(1);
			return "";
		}
	}
	
	public static String[] getTypeList() {
		ArrayList<String> typeList = new ArrayList<String>();
		
		for(PlayerType playerType: PlayerType.values()) {
			typeList.add(playerType.toString());
		}
		
		// Transform the ArrayList to a String array
		return typeList.toArray(new String[0]);
	}
	
	public static PlayerType indexToType(int index) {
		switch (index) {
		case 0:
			return PlayerType.HumanPlayer;
		case 1:
			return PlayerType.RandomAI;
		default:
			System.out.println("Invalid type index");
			System.exit(1);
			return PlayerType.HumanPlayer;
		}
	}
	
	public Player turnPlayerInto(Player player) {
		switch (this) {
		case HumanPlayer:
			return new HumanPlayer(player);
		case RandomAI:
			return new RandomAI(player);
		default:
			System.out.println("Invalid player type");
			System.exit(1);
			return player;
		}
	}
}

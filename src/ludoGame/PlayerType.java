package ludoGame;

import java.util.ArrayList;

public enum PlayerType {
	HumanPlayer,
	RandomAI,
	SpeedrunAI,
	SlowAI;
	
	public String toString() {
		switch (this) {
		case HumanPlayer:
			return "Human Player";
		case RandomAI:
			return "Random AI";
		case SpeedrunAI:
			return "Speedrun AI";
		case SlowAI:
			return "Slow AI";
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
		case 2:
			return PlayerType.SpeedrunAI;
		case 3:
			return PlayerType.SlowAI;
		default:
			System.out.println("Invalid type index");
			System.exit(1);
			return PlayerType.HumanPlayer;
		}
	}
	
	public int toIndex() {
		switch (this) {
		case HumanPlayer:
			return 0;
		case RandomAI:
			return 1;
		case SpeedrunAI:
			return 2;
		case SlowAI:
			return 3;
		default:
			System.out.println("Invalid player type");
			System.exit(1);
			return 1;
		}
	}
	
	public Player turnPlayerInto(Player player) {
		switch (this) {
		case HumanPlayer:
			return new HumanPlayer(player);
		case RandomAI:
			return new RandomAI(player);
		case SpeedrunAI:
			return new SpeedrunAI(player);
		case SlowAI:
			return new SlowAI(player);
		default:
			System.out.println("Invalid player type");
			System.exit(1);
			return player;
		}
	}
}

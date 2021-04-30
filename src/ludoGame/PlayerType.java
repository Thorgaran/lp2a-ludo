package ludoGame;

import java.util.ArrayList;

public enum PlayerType {
	RandomAI,
	SpeedrunAI,
	SlowAI,
	AggressiveAI,
	SmartAI,
	HumanPlayer,
	RemotePlayer;
	
	public String toString() {
		switch (this) {
		case RandomAI:
			return "Random AI";
		case SpeedrunAI:
			return "Speedrun AI";
		case SlowAI:
			return "Slow AI";
		case AggressiveAI:
			return "Aggressive AI";
		case SmartAI:
			return "Smart AI";
		case HumanPlayer:
			return "Human Player";
		case RemotePlayer:
			return "Remote Player";
		default:
			System.out.println("Invalid player type");
			System.exit(1);
			return "";
		}
	}
	
	// Returns a list of all the enum variants besides "RemotePlayer" called with .toString()
	public static String[] getTypeList() {
		ArrayList<String> typeList = new ArrayList<String>();
		
		for(PlayerType playerType: PlayerType.values()) {
			if (playerType != PlayerType.RemotePlayer) {
				typeList.add(playerType.toString());
			}
		}
		
		// Transform the ArrayList to a String array
		return typeList.toArray(new String[0]);
	}
	
	// Conversion between comboBox index and PlayerType
	public static PlayerType indexToType(int index) {
		switch (index) {
		case 0:
			return PlayerType.RandomAI;
		case 1:
			return PlayerType.SpeedrunAI;
		case 2:
			return PlayerType.SlowAI;
		case 3:
			return PlayerType.AggressiveAI;
		case 4:
			return PlayerType.SmartAI;
		case 5:
			return PlayerType.HumanPlayer;
		case 6:
			return PlayerType.RemotePlayer;
		default:
			System.out.println("Invalid type index");
			System.exit(1);
			return PlayerType.HumanPlayer;
		}
	}
	
	// Conversion into comboBox index
	public int toIndex() {
		switch (this) {
		case RandomAI:
			return 0;
		case SpeedrunAI:
			return 1;
		case SlowAI:
			return 2;
		case AggressiveAI:
			return 3;
		case SmartAI:
			return 4;
		case HumanPlayer:
			return 5;
		case RemotePlayer:
			return 6;
		default:
			System.out.println("Invalid player type");
			System.exit(1);
			return 1;
		}
	}
	
	// Takes a player and returns a derived inherited player
	public Player turnPlayerInto(Player player) {
		switch (this) {
		case RandomAI:
			return new RandomAI(player);
		case SpeedrunAI:
			return new SpeedrunAI(player);
		case SlowAI:
			return new SlowAI(player);
		case AggressiveAI:
			return new AggressiveAI(player);
		case SmartAI:
			return new SmartAI(player);
		case HumanPlayer:
			return new HumanPlayer(player);
		case RemotePlayer:
			return new RemotePlayer(player);
		default:
			System.out.println("Invalid player type");
			System.exit(1);
			return player;
		}
	}
}

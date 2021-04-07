package ludoGame;

public enum SquareType {
	Normal,
	Home,
	Start,
	Safe,
	Fork,
	GoalRow,
	Goal;
	
	public char toChar() {
		switch (this) {
		case Normal:
			return ' ';
		case Home:
			return 'H';
		case Start:
			return '*';
		case Safe:
			return '*';
		case Fork:
			return ' ';
		case GoalRow:
			return ' ';
		case Goal:
			return '+';
		default:
			return '?';
		}
	}
}

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
			return '-';
		case Home:
			return 'H';
		case Start:
			return 'S';
		case Safe:
			return '*';
		case Fork:
			return 'F';
		case GoalRow:
			return 'X';
		case Goal:
			return 'G';
		default:
			return '?';
		}
	}
}

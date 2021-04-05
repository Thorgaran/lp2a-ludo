package ludoGame;

public class Dice {
	private int number;
	
	Dice(){
		this.number = 0;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public void roll() {
		this.number = (int) (1 + (Math.random() * (6 - 1)));
	}
	
	public void dispFace() {
		//to print the dice once the graphical interface is made
		switch (this.number) {
		case 1: System.out.println("1");
		break;
		case 2: System.out.println("2");
		break;
		case 3: System.out.println("3");
		break;
		case 4: System.out.println("4");
		break;
		case 5: System.out.println("5");
		break;
		case 6: System.out.println("6");
		break;
		default: System.out.println("Error: dice was not rolled yet");
		}
	}
}

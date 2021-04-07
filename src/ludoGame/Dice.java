package ludoGame;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
	private int number;
	
	Dice() {
		this.number = 0;
	}
	
	public int roll() {
		this.number = ThreadLocalRandom.current().nextInt(1, 7);
		return this.number;
	}
	
	public void dispFace() {
		// To print the dice once the graphical interface is made
		switch (this.number) {
		case 1: System.out.println("Dice: 1");
			break;
		case 2: System.out.println("Dice: 2");
			break;
		case 3: System.out.println("Dice: 3");
			break;
		case 4: System.out.println("Dice: 4");
			break;
		case 5: System.out.println("Dice: 5");
			break;
		case 6: System.out.println("Dice: 6");
			break;
		default: System.out.println("Error: dice was not rolled yet");
		}
	}
}

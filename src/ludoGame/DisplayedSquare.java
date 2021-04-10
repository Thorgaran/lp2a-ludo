package ludoGame;

import javax.swing.*;
import java.awt.*;

public class DisplayedSquare extends JPanel {
	private Square square;
	
	DisplayedSquare(Square square) {
		this.square = square;
		
		if (square.getType() == SquareType.Normal || square.getType() == SquareType.Fork) {
			this.setBackground(Color.WHITE);
		}
		else {
			this.setBackground(square.getColor());
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		for(Token token: square.getTokens()) {
			g2d.setColor(token.getColor().darker());
			g2d.fillOval(15, 30 - 5*token.getHeight(), 20, 15);
		}
	}
}

package ludoGame;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

import com.therolf.miniServer.Message;
import com.therolf.miniServer.MessageType;

// This AI decides its move randomly among the available ones
public class RemotePlayer extends Player {
	RemotePlayer(Color color, ArrayList<Square> homes) {
		super(color, homes, PlayerType.RemotePlayer);
	}
	
	RemotePlayer(Player oldPlayer) {
		super(oldPlayer, PlayerType.RemotePlayer);
	}
	
	protected Token chooseToken(boolean showBoard, HashMap<Token, Square> playableTokens) {
		if (!showBoard) {
			System.out.println("Error! Remote player cannot be called with showBoard=false!");
			System.exit(1);
		}
		
		Token chosenToken = null;
		boolean validTokenChoice = false;
		
		try {
			while (!Game.getClient().isReady()) {
				Game.sleep(true, 100);
			}
			
			Message serverMessage = Game.getClient().read();
			
			if (serverMessage.getType() != MessageType.Token) {
				System.out.println("Error! Expected Token message, got " + serverMessage + "!");
				System.exit(1);
			}
			else {
				if (!serverMessage.getContents().equals("null")) {
					String tokenIndexStr = serverMessage.getContents();
					
					int tokenIndex = Integer.parseInt(tokenIndexStr);
					
					if (tokenIndex < 0 || tokenIndex >= 4) {
						System.out.println("Error! Token index out of bounds!");
						System.exit(1);
					}
					
					chosenToken = this.getTokens()[tokenIndex];
					
					if (!playableTokens.keySet().contains(chosenToken)) {
						System.out.println("Error! Unplayable token!");
						System.exit(1);
					}
				}
				
				validTokenChoice = true;
			}
		} catch (IOException e) {
			System.out.println("IO error!");
			e.printStackTrace();
			System.exit(1);
		}
		
		if (!validTokenChoice) {
			System.out.println("Error! Invalid token choice!");
			System.exit(1);
		}
		
		return chosenToken;
	}
}

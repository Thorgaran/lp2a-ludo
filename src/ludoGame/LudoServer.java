package ludoGame;

import com.therolf.miniServer.MessageType;
import com.therolf.miniServer.Server;

import java.awt.Color;
import java.util.*;

public class LudoServer {
	private Random randGen = new Random();
	
	//private ArrayList<Color> colorsLeft = new ArrayList<Color>(Arrays.asList(Game.PLAYER_COLORS));
	private ArrayList<Color> colorsLeft = new ArrayList<Color>(Arrays.asList(Game.SW_COLOR, Game.NE_COLOR));
	private HashMap<Color, PlayerType> playerTypes = new HashMap<Color, PlayerType>();
	
    public LudoServer() {
        String ip = null;        
        System.out.print("Enter server IP: ");
        Scanner sc = new Scanner(System.in);
        if(sc.hasNextLine())
            ip = sc.nextLine();

        int port = 2234;
        System.out.print("Enter server port: ");
        if(sc.hasNextInt())
            port = sc.nextInt();
        
        // TEMP ---------------------------------------------------------------------------
        for (Color color: Game.PLAYER_COLORS) {
        	this.playerTypes.put(color, PlayerType.SmartAI);
        }
        // --------------------------------------------------------------------------------
        
        Server ludoServer = new Server(port, "LudoServer", ip);
        
        ludoServer.setMessageListener((fromPseudo, type, contents) -> {
            switch (type) {
            case JoinGame:
            	// Place Human player at random color
            	Color randColor = this.colorsLeft.remove(this.randGen.nextInt(this.colorsLeft.size()));
            	this.playerTypes.put(randColor, PlayerType.HumanPlayer);
            	
            	// Acknowledge join game request
            	ludoServer.sendFromTo(ludoServer.getServerName(), fromPseudo, type, "");
            	
            	// If all humans got their positions and are ready to start
            	if (this.colorsLeft.isEmpty()) {
            		ArrayList<Color> humansSent = new ArrayList<Color>();
            		boolean isFirstClient = true;
            		for(String client: ludoServer.getAllPseudos()) {
            			// Temporary copy of playerTypes
            			@SuppressWarnings("unchecked")
						HashMap<Color, PlayerType> clientPlayerTypes = (HashMap<Color, PlayerType>) this.playerTypes.clone();
            			
        				Boolean foundHuman = false;
        				for (Color color: Game.PLAYER_COLORS) {
        					if (this.playerTypes.get(color) == PlayerType.HumanPlayer
        						&& !foundHuman && !humansSent.contains(color)
        					) {
    							foundHuman = true;
    							humansSent.add(color);
        					}
        					else if (!isFirstClient || (isFirstClient && foundHuman && this.playerTypes.get(color) == PlayerType.HumanPlayer)) {
        						// Remove AIs unless it's the first client (which will handle them)
        						clientPlayerTypes.put(color, PlayerType.RemotePlayer);
        					}
        				}
            			
            			isFirstClient = false;
            			
            			// Turn types to string
            			StringBuffer typesAsString = new StringBuffer("");
            			for (Color color: Game.PLAYER_COLORS) {
            				// Append type as index
            				typesAsString.append(clientPlayerTypes.get(color).toIndex());

        					// Append separator
        					typesAsString.append(", ");
            			}
            			
            			// Send types string
            			ludoServer.sendFromTo(ludoServer.getServerName(), client, 
            				MessageType.PlayerTypes, typesAsString.toString());
                    }
            		
            		// Start game
            		ludoServer.sendToEveryone(ludoServer.getServerName(), MessageType.GameStart, "");
            	}
            	break;
            
            case AskRoll:
            	String rollValueStr = String.valueOf(this.randGen.nextInt(6) + 1);
            	
            	ludoServer.sendToEveryone(ludoServer.getServerName(), MessageType.DiceRoll, rollValueStr);
            	break;
            	
			case Chat:
				break;
			
			case Token:
				ludoServer.sendToEveryoneElse(fromPseudo, type, contents);
				break;
			
			default:
				System.out.println("Received unexpected message type '" 
					+ type.name() + "' from " + fromPseudo);
				break;
            }
        });
        ludoServer.run();
        
        sc.close();
    }

    public static void main(String[] args) {
        new LudoServer();
    }
}

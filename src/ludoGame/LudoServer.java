package ludoGame;

import com.therolf.miniServer.MessageType;
import com.therolf.miniServer.Server;

import java.util.*;

public class LudoServer {
	private Random randGen = new Random();
	
	private ArrayList<String> positionsLeft = new ArrayList<String>(
		Arrays.asList("NW", "SW", "NE", "SE"));
	
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
        
        Server ludoServer = new Server(port, "LudoServer", ip);
        
        ludoServer.setMessageListener((fromPseudo, type, contents) -> {
            switch (type) {
            case AskPosition:
            	// Pop position from the available ones
            	String position = this.positionsLeft.remove(
            		this.randGen.nextInt(this.positionsLeft.size()));
            	
            	// Send it to the client
            	ludoServer.sendFromTo(ludoServer.getServerName(), fromPseudo, 
            			MessageType.StartPosition, position);
            	
            	// If 4 players got their positions and are ready to start
            	if (this.positionsLeft.isEmpty()) {
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
                
            
            	/*case "hello":
                    miniServer.sendToEveryoneElse(fromPseudo, fromPseudo + " says hello");
                    break;
                case "list":
                    miniServer.sendFromTo(miniServer.getServerName(), fromPseudo, miniServer.getAllPseudos());
                    break;
                case "ping":
                    miniServer.sendFromTo(miniServer.getServerName(), fromPseudo, "pong");
                    break;
                default:
                	ludoServer.sendToEveryone(fromPseudo, message);
                    break;*/
            }
        });
        ludoServer.run();
        
        sc.close();
    }

    public static void main(String[] args) {
        new LudoServer();
    }
}

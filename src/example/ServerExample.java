package example;

import com.therolf.miniServer.Server;

import java.util.Scanner;

public class ServerExample {
    public static final int PORT = 2234;

    public ServerExample() {
        String ip = null;
        System.out.println("Enter server IP :");
        Scanner sc = new Scanner(System.in);
        if(sc.hasNextLine())
            ip = sc.nextLine();

        Server miniServer = new Server(PORT, "SevenWonders", ip);
        miniServer.setMessageListener((fromPseudo, type, contents) -> {
        	switch (type) {
        	case Chat:
        		switch (contents) {
        		case "hello":
        			miniServer.sendToEveryoneElse(fromPseudo, type, fromPseudo + " says hello");
        			break;
        		case "list":
        			miniServer.sendFromTo(miniServer.getServerName(), fromPseudo, type, miniServer.getAllPseudos());
        			break;
        		case "ping":
        			miniServer.sendFromTo(miniServer.getServerName(), fromPseudo, type, "pong");
        			break;
        		default:
        			miniServer.sendToEveryone(fromPseudo, type, contents);
        			break;
        		}
			default:
				break;
        	}
        });
        miniServer.run();
        
        sc.close();
    }

    public static void main(String[] args) {
        new ServerExample();
    }
}

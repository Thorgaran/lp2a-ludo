package example;

import com.therolf.miniServer.Client;
import com.therolf.miniServer.Message;
import com.therolf.miniServer.MessageType;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

public class ClientExample {
    private Client client;

    public ClientExample() {
        // start scanner
        Scanner sc = new Scanner(System.in);
        String pseudo = ""; // = ""

        // starting program
        System.out.println("=== Mini Client example ===");
        String ip = ""; // = ""
        System.out.println("Please enter ip address:");
        if(sc.hasNextLine())
            ip = sc.nextLine();
//        ip = "127.0.0.1";
        int port = ServerExample.PORT;

        // trying to connect
        System.out.println("trying to connect to " + ip + ":" + port);
        try {
            client = new Client(ip, port);

            System.out.println("Please Login with your pseudo: ");

            // auth
            while(!client.isAuthed() && sc.hasNextLine()) {
                pseudo = sc.nextLine();
                client.send(MessageType.Login, pseudo);
                
                Message serverResponse = client.read();
                if (serverResponse.getType() == MessageType.AuthSuccessful) {
                	System.out.println(
                		"Authencation successful! Now logged in as " + serverResponse.getContents());
                }
                else {
                	System.out.println(serverResponse.getContents());
                }
            }
            System.out.println("=== Successfully logged in as " + pseudo + " ===");

            // send messages
            new Thread(() -> {
                while(client.isRunning()) {
                    if(sc.hasNextLine())
                    {
                        String line = sc.nextLine();
                        if(line.equals("bye")) {
                            client.send(MessageType.ShutDownCommand);
                        } else {
                            client.send(MessageType.Chat, line);
                        }
                    }
                }
            }).start();

            // receive messages
            while(client.isRunning()) {
                if(client.isReady()) {
                    Message m = client.read();
                    if (m.getType() == MessageType.Chat) {
                    	System.out.println(m.getPseudo() + ": " + m.getContents());
                    }
                    else {
                    	System.out.println(m);
                    }
                }
            }

            //properly close client
            if(!client.isClosed())
                client.close();
        }
        catch (ConnectException e) {
            System.err.println("Couldn't connect to " + ip + ":" + port);
            System.err.println(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=== bye ===");
        sc.close();
    }

    public static void main(String[] args) {
        new ClientExample();
    }
}

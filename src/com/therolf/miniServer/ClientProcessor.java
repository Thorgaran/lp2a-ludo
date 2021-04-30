package com.therolf.miniServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcessor implements Runnable {
    private Socket socket;
    private Server server;
    private PrintWriter pw;
    private BufferedReader br;
    private String pseudo;

    public String getPseudo() {
        return pseudo;
    }

    private ClientProcessor(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        System.out.println("new connection");
        try {

            // start writer and reader
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while ((server.connectionsLimit == -1 || server.personsConnected <= server.connectionsLimit) && !socket.isClosed()) {
//                System.out.println(socket.isClosed() + " + " + socket.isConnected() + " + " + socket.isInputShutdown() + socket.isOutputShutdown());
                if (br != null && br.ready()) {
                    String input = read();
                    // Decode message
                    Message m = Message.fromString(input);
                    
                    if (m.type == MessageType.ShutDownCommand) {
                        if (pseudo != null && server.authListener != null) {
                            --server.personsConnected;
                            server.authListener.OnConnectionEnds(pseudo , server.personsConnected);
                        }
                        socket.close();
                    } else if (pseudo == null) {
                    	if (m.type != MessageType.Login) {
                    		send(server.getServerName(), MessageType.AuthFailed, 
                    			"Auth failed. Please send a login message first.");
                    	}
                    	else if (server.pseudoExists(m.contents)) {
                    		// if pseudo already exists, notify with error
                            send(server.getServerName(), MessageType.AuthFailed, 
                            	"Auth failed. Please choose another pseudo.");
                        } else {
                            //change pseudo
                            pseudo = m.contents;

                            ++server.personsConnected;
                            if(server.authListener != null) {
                                server.authListener.OnNewConnection(pseudo, server.personsConnected);
                            }

                            send(server.getServerName(), MessageType.AuthSuccessful, pseudo);
                            System.out.println("connection successful with " + pseudo);
                        }
                    } else if(server.messageListener != null) {
                    	// The user is authed here
                    	server.messageListener.onMessageReceived(m.getPseudo(), m.type, m.contents);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("end of connection");
        System.out.println((pseudo != null) ? " with " + pseudo : "");
        server.removeClient(ClientProcessor.this);
        System.out.println(server.getOpenSessions() + " open sessions left");
    }

    static ClientProcessor addNewProcessor(Socket socket, Server server) {
	    ClientProcessor c = new ClientProcessor(socket, server);
        new Thread(c).start();
        return c;
    }

    public void send(String fromPseudo, MessageType type, String message) {
        if(pw != null)
            pw.println(Message.toString(fromPseudo, type, message));
    }

    public void sendIfAuthed(String fromPseudo, MessageType type, String string) {
        if(this.pseudo != null)
            this.send(fromPseudo, type, string);
    }

    private String read() throws IOException {
        if(br != null) {
            return br.readLine();
        }
        throw new IOException("No br defined");
    }
}

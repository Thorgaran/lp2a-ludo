package com.therolf.miniServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private BufferedReader br;
    private PrintWriter pw;
    private Socket socket;
    private boolean isAuthed = false;
    private String pseudo;

    public Client(String ip, int port) throws IOException {

        this.socket = new Socket(ip, port);
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.pw = new PrintWriter(socket.getOutputStream(), true);

        /*
        TRUC MEGA IMPORTANT NE PAS SUPPRIMER
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.send(MessageType.ShutDownCommand)));
    }
    
    public void send(MessageType type, String contents) {
        if(pw != null) {
            // close server properly
            if (type == MessageType.ShutDownCommand) {
                pw.println(contents);
                pw.close();
            } else if (pseudo != null) {
                pw.println(Message.toString(pseudo, type, contents));
            } else {
                pw.println(Message.toString("Unlogged user", type, contents));
            }
        }
    }
    
    public void send(MessageType type) {
    	this.send(type, "");
    }

    public Message read() throws IOException {
        Message m; // = null
        if(br != null) {
            m = Message.fromString(br.readLine());
            if(m.type == MessageType.AuthSuccessful) {
                isAuthed = true;
                this.pseudo = m.contents;
            }
        } else {
            throw new IOException("No br defined");
        }

        return m;
    }
    
    public String expect(MessageType expectedType) {
    	Message m = null;
		try {
			m = this.read();
		} catch (IOException e) {
			System.out.println("IO Error!");
			e.printStackTrace();
			System.exit(1);
		}
    	
    	if (m.getType() != expectedType) {
    		System.err.println("Error, wrong message type! Expected " + expectedType.name()
    			+ ", got " + m.getType().name());
        	System.exit(1);
    	}
    	
    	return m.contents;
    }

    public boolean isAuthed() {
        return isAuthed;
    }

    public void close() {
        if(pw != null)
            pw.close();
        if(br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(socket != null) {
            try {
                socket.close();
                System.out.println("closed socket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isClosed() {
        return socket.isClosed();
    }

    public boolean isRunning() {
        return !isClosed();
    }

    public boolean isReady() throws IOException {
        return br.ready();
    }
}

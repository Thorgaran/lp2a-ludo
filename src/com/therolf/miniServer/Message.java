package com.therolf.miniServer;

public class Message {
    static final String MESSAGE_SEPARATOR = " | ";
    String pseudo, contents;
    MessageType type;

    @SuppressWarnings("unused")
    public String getPseudo() {
        return pseudo;
    }
    
    @SuppressWarnings("unused")
    public MessageType getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public String getContents() {
        return contents;
    }

    public Message(String pseudo, MessageType type, String contents) {
        this.pseudo = pseudo;
        this.type = type;
        this.contents = contents;
    }

    public static Message fromString(String inputString) {
        int separatorIndex1 = inputString.indexOf(Message.MESSAGE_SEPARATOR);
        int separatorIndex2 = inputString.lastIndexOf(Message.MESSAGE_SEPARATOR);
        String pseudo = "unknown";
        MessageType type = MessageType.InvalidType;
        String contents = inputString;
        
        
        if (separatorIndex1 > -1) {
            pseudo = inputString.substring(0, separatorIndex1);
            
            if (separatorIndex2 != separatorIndex1) {
            	type = MessageType.fromString(inputString.substring(
                	separatorIndex1 + MESSAGE_SEPARATOR.length(), separatorIndex2));
            	
            	contents = inputString.substring(separatorIndex2 + MESSAGE_SEPARATOR.length());
            }
            else {
            	type = MessageType.fromString(
            		inputString.substring(separatorIndex1 + MESSAGE_SEPARATOR.length()));
            	contents = "";
            }
        }

        return new Message(pseudo, type, contents);
    }

    static String toString(String pseudo, MessageType type, String contents) {
        return pseudo + Message.MESSAGE_SEPARATOR + type.name() + Message.MESSAGE_SEPARATOR + contents;
    }
}

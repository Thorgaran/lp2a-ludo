package com.therolf.miniServer;

public enum MessageType {
	ShutDownCommand,
	Login,
	AuthFailed,
	AuthSuccessful,
	AskPosition,
	StartPosition,
	GameStart,
	AskRoll,
	DiceRoll,
	Token,
	Chat,
	InvalidType;
	
	public static MessageType fromString(String typeAsString) {
		switch (typeAsString) {
		case "ShutDownCommand":
			return MessageType.ShutDownCommand;
		case "Login":
			return MessageType.Login;
		case "AuthFailed":
			return MessageType.AuthFailed;
		case "AuthSuccessful":
			return MessageType.AuthSuccessful;
		case "AskPosition":
			return MessageType.AskPosition;
		case "StartPosition":
			return MessageType.StartPosition;
		case "GameStart":
			return MessageType.GameStart;
		case "AskRoll":
			return MessageType.AskRoll;
		case "DiceRoll":
			return MessageType.DiceRoll;
		case "Token":
			return MessageType.Token;
		case "Chat":
			return MessageType.Chat;
		default:
			return MessageType.InvalidType;
		}
	}
}

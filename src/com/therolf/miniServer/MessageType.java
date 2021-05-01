package com.therolf.miniServer;

public enum MessageType {
	ShutDownCommand,
	Login,
	AuthFailed,
	AuthSuccessful,
	JoinGame,
	PlayerTypes,
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
		case "JoinGame":
			return MessageType.JoinGame;
		case "PlayerTypes":
			return MessageType.PlayerTypes;
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

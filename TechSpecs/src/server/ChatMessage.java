package server;

import java.io.Serializable;

public class ChatMessage extends Message implements Serializable {
	private static final long serialVersionUID = 5218199986503806517L;

	public ChatMessage(String username, String message) {
		super(username, message);
	}
}

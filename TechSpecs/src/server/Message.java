package server;

public class Message extends NetworkMessage {

	public static final long serialVersionUID = 1;

	private String message;
	private String username;

	public Message(String username, String message) {
		this.username = username;
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public String getUsername() {
		return this.username;
	}
}

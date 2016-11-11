package game_logic;

import java.io.Serializable;

//implements Serializable
public class User implements Serializable {
	private static final long serialVersionUID = -2655907164924446350L;
	private String password;
	private String username;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

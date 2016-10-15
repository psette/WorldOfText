package game_logic;

import java.io.Serializable;
import java.util.ArrayList;

public class UsersDB implements Serializable {
	private static final long serialVersionUID = -6156438447901219548L;
	ArrayList<String> users;
	ArrayList<String> passwords;

	public UsersDB() {
		users = new ArrayList<>();
		passwords = new ArrayList<>();
	}

	public void addUser(String user, String password) {
		users.add(user);
		passwords.add(password);
	}

	public boolean hasUser(String user) {
		return users.contains(user);
	}

	public boolean userPassCombo(String user, String password) {
		if (!hasUser(user)) {
			return false;
		}
		return password.equals(passwords.get(users.indexOf(user)));
	}
}
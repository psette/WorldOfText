package main;

import frames.LoginGUI;
import server.Server;

public class RunClients {

	public static void main(String[] args) {
		new Server(6789);
		new LoginGUI().setVisible(true);
		new LoginGUI().setVisible(true);
	}

}

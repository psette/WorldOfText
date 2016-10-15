package main;

import frames.LoginWindow;
import frames.StartWindowGUI;

public class Main {
	static boolean debug = true;

	public static void main(String[] args) {
		if (!debug) {
			new LoginWindow();
		} else
			new StartWindowGUI(new LoginWindow());
	}
}

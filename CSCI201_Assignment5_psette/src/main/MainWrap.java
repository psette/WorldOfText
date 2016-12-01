package main;

import java.io.File;

import frames.StartWindowGUI;
import game_logic.User;

public class MainWrap {
	public MainWrap() {
		StartWindowGUI sw1 = new StartWindowGUI(new User("a", "a"));
		sw1.notNetworked.setSelected(false);
		sw1.hostGame.setSelected(true);
		sw1.port.setText("1239");
		sw1.slider.setValue(2);
		sw1.teamNameTextFields.get(0).setText("Team 1");
		sw1.wrapper(new File("/Users/pietrosette/Downloads/Jeopardy Assignment 4 Updated 2/game_files/gamefile1.txt"));
		sw1.startGameButton.doClick();
		StartWindowGUI sw2 = new StartWindowGUI(new User("b", "b"));
		sw2.notNetworked.setSelected(false);
		sw2.IPAddress.setText("localhost");
		sw2.joinGame.setSelected(true);
		sw2.port.setText("1239");
		sw2.teamNameTextFields.get(0).setText("Team 2");
		sw2.startGameButton.doClick();

	}

	public static void main(String[] args) {
		new MainWrap();
	}
}

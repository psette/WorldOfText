package network_messages;

import frames.MainGUI;
import game_logic.GameData;

public class ChangeToFinal extends NetworkMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556537091464040758L;
	private GameData data;
	private MainGUI gui;
	private boolean finalJep;

	public ChangeToFinal(boolean changeToFinal, GameData gameData, MainGUI mainGUI) {
		setFinalJep(changeToFinal);
		setData(gameData);
		setGui(mainGUI);
	}

	public GameData getData() {
		return data;
	}

	public void setData(GameData data) {
		this.data = data;
	}

	public MainGUI getGui() {
		return gui;
	}

	public void setGui(MainGUI gui) {
		this.gui = gui;
	}

	public boolean getID() {
		return finalJep;
	}

	public void setFinalJep(boolean finalJep) {
		this.finalJep = finalJep;
	}

}

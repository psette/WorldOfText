package network_messages;

import game_logic.GameData;

public class AddGameData extends NetworkMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3277631130492831074L;
	private GameData data;

	public AddGameData(GameData data) {
		this.data = data;
	}

	public GameData getData() {
		return data;
	}

}
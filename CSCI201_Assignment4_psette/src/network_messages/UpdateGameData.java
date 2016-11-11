package network_messages;

import game_logic.GameData;

public class UpdateGameData extends NetworkMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8178878082162295805L;
	private GameData data;

	public UpdateGameData(GameData data) {
		this.setData(data);
	}

	public GameData getData() {
		return data;
	}

	public void setData(GameData data) {
		this.data = data;
	}
}

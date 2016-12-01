package messages;

public class PlayerLeftMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4224084652950297812L;
	private int team;

	public PlayerLeftMessage(int team) {
		this.team = team;
	}

	public int getTeamWhoLeft() {
		return team;
	}
}

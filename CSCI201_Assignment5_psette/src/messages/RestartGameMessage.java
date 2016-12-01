package messages;

public class RestartGameMessage implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 317443618805757740L;
	private int firstTeam;

	public RestartGameMessage(int team) {
		this.firstTeam = team;
	}

	public int getFirstTeam() {
		return firstTeam;
	}
}

package messages;

public class BuzzInMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1163494414610204214L;
	// all we need to know is which team buzzed in
	private int team;

	public BuzzInMessage(int team) {
		this.team = team;
	}

	public int getBuzzInTeam() {
		return team;
	}
}

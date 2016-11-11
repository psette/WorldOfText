package network_messages;

public class CorrectAnswer extends NetworkMessage {
	private static final long serialVersionUID = -2542699106207781026L;
	private int pointValue;
	private String team;

	public CorrectAnswer(int pointValue, String currentTeam) {
		this.setPointValue(pointValue);
		setTeam(currentTeam);
	}

	public String getTeam() {
		return team;
	}

	private void setTeam(String team) {
		this.team = team;
	}

	public int getPointValue() {
		return pointValue;
	}

	private void setPointValue(int pointValue) {
		this.pointValue = pointValue;
	}

}

package messages;

public class FJAnswerMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3758301081782182348L;
	// all we need to know is which team this is, and what answer they gave
	private String answer;
	private int team;

	public FJAnswerMessage(String answer, int team) {
		this.team = team;
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}

	public int getTeamThatAnswered() {
		return team;
	}
}

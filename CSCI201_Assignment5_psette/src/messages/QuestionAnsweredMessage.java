package messages;

public class QuestionAnsweredMessage implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5197954572825111240L;
	private String answer;

	public QuestionAnsweredMessage(String answer) {
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}
}

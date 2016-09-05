
public class Team {
	private String name_;
	private int points_;
	private int final_jeopardy_ = 0;
	private boolean warned_;

	public Team(String name) {
		name_ = name;
		points_ = 0;
		final_jeopardy_ = 0;
		warned_ = false;
	}

	public void ModifyPoints(int change) {
		points_ += change;
	}

	public void modifyFinalJeopardy(int ammount) {
		final_jeopardy_ = ammount;
	}

	public void correctFinal() {
		points_ += final_jeopardy_;
	}

	public void wrongFinal() {
		points_ += final_jeopardy_;
	}

	public int getPoints() {
		return points_;
	}

	public String getName() {
		return name_;
	}

	public boolean alreadyWarned() {
		if (!warned_) {
			warned_ = true;
			return false;
		}
		return true;
	}
}

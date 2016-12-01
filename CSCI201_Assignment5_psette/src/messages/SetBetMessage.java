package messages;

public class SetBetMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2809312735072294502L;
	private int team;
	private int bet;

	public SetBetMessage(int team, int bet) {
		this.team = team;
		this.bet = bet;
	}

	public int getTeamThatBet() {
		return team;
	}

	public int getBet() {
		return bet;
	}
}

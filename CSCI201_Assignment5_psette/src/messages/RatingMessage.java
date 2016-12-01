package messages;

public class RatingMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8737828442621792385L;
	private int rating;
	private int team;

	public RatingMessage(int rating, int team) {
		this.rating = rating;
		this.team = team;
	}

	public int getRating() {
		return rating;
	}

	public int getTeamWhoRated() {
		return team;
	}
}

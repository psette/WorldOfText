package network_messages;

public class AddTeams extends NetworkMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3855819619668421160L;
	private String teamName;

	public AddTeams(String teamName) {
		this.setTeamName(teamName);
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

}
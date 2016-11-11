package network_messages;

public class AddUpdate extends NetworkMessage {
	private String update;

	public AddUpdate(String update) {
		this.setUpdate(update);
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

}

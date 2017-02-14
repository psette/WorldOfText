package server;

public class RemoveUpdate extends NetworkMessage {
	private static final long serialVersionUID = 4156065212270509558L;

	private int removeHere;

	public RemoveUpdate(int start) {
		setRemoveHere(start);
	}

	public int getRemoveHere() {
		return removeHere;
	}

	public void setRemoveHere(int removeHere) {
		this.removeHere = removeHere;
	}

}

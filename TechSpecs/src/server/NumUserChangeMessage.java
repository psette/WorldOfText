package server;

public class NumUserChangeMessage extends NetworkMessage {
	private static final long serialVersionUID = 1L;
	private int numUsers;
	public NumUserChangeMessage(int numUsers) {
		this.numUsers = numUsers;
	}
	public int getNumUsers(){
		return numUsers;
	}
}

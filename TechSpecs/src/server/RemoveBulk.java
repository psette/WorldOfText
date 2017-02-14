package server;

public class RemoveBulk extends NetworkMessage {

	private static final long serialVersionUID = 789603584701359226L;
	private int start;
	private int offset;

	public RemoveBulk(int start, int offset) {
		setStart(start);
		setOffset(offset);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}

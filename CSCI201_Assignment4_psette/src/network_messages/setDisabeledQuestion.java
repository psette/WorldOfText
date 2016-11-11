package network_messages;

public class setDisabeledQuestion extends NetworkMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4204897711456772463L;
	private boolean isDisabled;
	private int x, y;

	public setDisabeledQuestion(boolean b, int indexX, int indexY) {
		setDisabled(b);
		setX(indexX);
		setY(indexY);
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
}

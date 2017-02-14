package server;

import java.util.Vector;

public class SendPaneInitial extends NetworkMessage {

	private static final long serialVersionUID = 2529671413048613902L;
	private Vector<CharacterStylePair> content;

	public SendPaneInitial(Vector<CharacterStylePair> arrayList) {
		setContent(arrayList);
	}

	public Vector<CharacterStylePair> getContent() {
		return content;
	}

	public void setContent(Vector<CharacterStylePair> content) {
		this.content = content;
	}

}

package server;

import java.io.Serializable;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;

public class Update extends NetworkMessage implements Serializable {

	private static final long serialVersionUID = -1470853843951761488L;

	private char character;
	private AttributeSet charAttr;
	private String updateType = "";
	private int position;
	private int startPos;
	private int length;

	public Update(char character, int place, Element el, String update) {
		updateType = update;
		setCharacter(character);
		setPosition(place);
		setCharacterAttribute(el);
	}

	public Update(int startPos, int length, AttributeSet attr, String update) {
		updateType = update;
		this.startPos = startPos;
		this.length = length;
		charAttr = attr;
	}

	public String getUpdateType() {
		if (updateType == null) {
			System.out.println("update type is null");
		}
		return updateType;
	}

	public int getStartPosition() {
		return startPos;
	}

	public int getLength() {
		return length;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public AttributeSet getCharacterAttribute() {
		return charAttr;
	}

	public void setCharacterAttribute(Element el) {
		charAttr = el.getAttributes();
	}
}
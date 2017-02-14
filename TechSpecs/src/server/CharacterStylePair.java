package server;

import java.io.Serializable;

import javax.swing.text.Element;

public class CharacterStylePair implements Serializable {
	private static final long serialVersionUID = 8576577737429807303L;
	private char character;
	private Element element;

	public CharacterStylePair(char character, Element element) {
		this.setCharacter(character);
		this.setElement(element);

	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}

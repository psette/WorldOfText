package network_messages;

import other_gui.QuestionGUIElement;

public class ChangePanel extends NetworkMessage {
	private QuestionGUIElement element;
	private static final long serialVersionUID = -6877314886268808833L;

	public ChangePanel(QuestionGUIElement element) {
		this.setElement(element);
	}

	public QuestionGUIElement getElement() {
		return element;
	}

	public void setElement(QuestionGUIElement element) {
		this.element = element;
	}

}

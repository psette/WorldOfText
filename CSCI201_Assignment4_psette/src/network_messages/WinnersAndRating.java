package network_messages;

import frames.WinnersAndRatingGUI;

public class WinnersAndRating extends NetworkMessage {

	private static final long serialVersionUID = 6843446050301127376L;

	private WinnersAndRatingGUI gui;

	public WinnersAndRating(WinnersAndRatingGUI andRatingGUI) {
		setGui(andRatingGUI);
	}

	public WinnersAndRatingGUI getGui() {
		return gui;
	}

	public void setGui(WinnersAndRatingGUI gui) {
		this.gui = gui;
	}

	/**
	 * 
	 */

}

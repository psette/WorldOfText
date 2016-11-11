package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game_logic.GameData;
import network_messages.AddGameData;
import network_messages.AddTeams;
import network_messages.AddUpdate;
import network_messages.ChangePanel;
import network_messages.ChangeToFinal;
import network_messages.CorrectAnswer;
import network_messages.NetworkMessage;
import network_messages.NextTurn;
import network_messages.UpdateNumberOfChosenQuestions;
import network_messages.WinnersAndRating;
import network_messages.setDisabeledQuestion;

public class ServerThread extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private GameServer gs;

	public ServerThread(Socket s, GameServer gs) {
		try {
			this.gs = gs;
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	public void sendMessage(NetworkMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	public void run() {
		try {
			while (true) {
				NetworkMessage message = (NetworkMessage) ois.readObject();
				if (message == null) {
					break;
				} else if (message instanceof AddTeams) {

					gs.addTeam(((AddTeams) message).getTeamName());

				} else if (message instanceof AddUpdate) {

					gs.updateText(((AddUpdate) message).getUpdate(), this);

				} else if (message instanceof CorrectAnswer) {

					gs.correctResponse(((CorrectAnswer) message).getPointValue(), ((CorrectAnswer) message).getTeam(),
							this);

				} else if (message instanceof ChangeToFinal) {

					if (((ChangeToFinal) message)
							.getID() /* change to final jep */) {
						gs.showFinalJeopardy(((ChangeToFinal) message).getData(), ((ChangeToFinal) message).getGui());
					} else {
						gs.showQuestionPage(((ChangeToFinal) message).getData(), ((ChangeToFinal) message).getGui());
					}

				} else if (message instanceof NextTurn) {
					gs.nextTurn(this);
				} else if (message instanceof UpdateNumberOfChosenQuestions) {
					gs.UpdateNumQuest(this);
				} else if (message instanceof WinnersAndRating) {
					gs.winnersGUI(((WinnersAndRating) message).getGui());
				} else if (message instanceof setDisabeledQuestion) {
					gs.setDisabledQuestion(((setDisabeledQuestion) message).isDisabled(),
							((setDisabeledQuestion) message).getX(), ((setDisabeledQuestion) message).getY(), this);
				} else if (message instanceof ChangePanel){
					gs.showPanel(((ChangePanel) message).getElement(), this);
				}

			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe in run: " + cnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}

}
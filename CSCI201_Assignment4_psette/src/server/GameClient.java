package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import frames.MainGUI;
import frames.StartWindowGUI;
import frames.WinnersAndRatingGUI;
import network_messages.AddTeams;
import network_messages.AddUpdate;
import network_messages.ChangePanel;
import network_messages.ChangeToFinal;
import network_messages.CorrectAnswer;
import network_messages.AddGameData;
import network_messages.NetworkMessage;
import network_messages.NextTurn;
import network_messages.UpdateNumberOfChosenQuestions;
import network_messages.WinnersAndRating;
import network_messages.setDisabeledQuestion;
import other_gui.FinalJeopardyGUI;

public class GameClient extends Thread {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private StartWindowGUI swg;
	private MainGUI mainGUI;
	private WinnersAndRatingGUI winnersAndRatingGUI;
	private boolean isHost = false;
	private String team;

	public GameClient(String hostname, int port, String teamName, StartWindowGUI swg) {
		setTeam(teamName);
		Socket s = null;
		this.swg = swg;
		try {
			s = new Socket(hostname, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(new AddTeams(teamName));
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	public void setHost() {
		isHost = true;
	}

	public void sendNetworkMessage(NetworkMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				NetworkMessage message = (NetworkMessage) ois.readObject();
				if (message instanceof AddGameData) {
					swg.dispose();
					mainGUI = new MainGUI(((AddGameData) message).getData(), null, true, this, isHost);
					mainGUI.setVisible(true);
				} else if (message instanceof AddUpdate) {

					mainGUI.addUpdateNonRecur(((AddUpdate) message).getUpdate());

				} else if (message instanceof CorrectAnswer) {

					correctAnswer(((CorrectAnswer) message).getPointValue(), ((CorrectAnswer) message).getTeam());

				} else if (message instanceof ChangeToFinal) {

					if (((ChangeToFinal) message)
							.getID() /* change to final jep */) {
						mainGUI.changePanel(new FinalJeopardyGUI(mainGUI.gameData, mainGUI));
					} else {
						mainGUI.showMainPanel();
					}

				} else if (message instanceof NextTurn) {
					mainGUI.gameData.nextTurn();
				} else if (message instanceof UpdateNumberOfChosenQuestions) {
					mainGUI.gameData.updateNumberOfChosenQuestions();
				} else if (message instanceof WinnersAndRating) {
					winnersAndRatingGUI = new WinnersAndRatingGUI(mainGUI.gameData);
					winnersAndRatingGUI.setVisible(true);
				} else if (message instanceof setDisabeledQuestion) {
					mainGUI.questionButtons[((setDisabeledQuestion) message).getX()][((setDisabeledQuestion) message)
							.getY()].setEnabled(false);
				} else if (message instanceof ChangePanel) {
					mainGUI.changePanel(((ChangePanel) message).getElement().questionPanel);
				}
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	private void correctAnswer(int value, String team) {
		for (int i = 0; i < mainGUI.gameData.getNumberOfTeams(); i++) {
			if (mainGUI.gameData.getTeamDataList().get(i).getTeamName().equals(team)) {
				mainGUI.gameData.getTeamDataList().get(i).addPoints(value);
			}
		}

	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}
}
package other_gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Timer;

import frames.MainGUI;
import frames.MainGUINetworked;
import game_logic.GameData;
import messages.BuzzInMessage;
import messages.QuestionAnsweredMessage;
import messages.QuestionClickedMessage;
import server.Client;

//inherits from QuestionGUIElement
public class QuestionGUIElementNetworked extends QuestionGUIElement {
	private static final long serialVersionUID = -6276366816659938513L;
	private Client client;
	// very similar variables as in the AnsweringLogic class
	public Boolean hadSecondChance;
	private TeamGUIComponents currentTeam;
	private TeamGUIComponents originalTeam;
	int teamIndex;
	int numTeams;
	public Timer clockTimer;
	private MainGUINetworked gui;
	// stores team index as the key to a Boolean indicating whether they have
	// gotten a chance to answer the question
	private HashMap<Integer, Boolean> teamHasAnswered;

	public QuestionGUIElementNetworked(String question, String answer, String category, int pointValue, int indexX,
			int indexY) {
		super(question, answer, category, pointValue, indexX, indexY);
		setWaitBuzz(false);
	}

	// set the client and also set the map with the booleans to all have false
	public void setClient(Client client, int numTeams) {
		this.client = client;
		this.numTeams = numTeams;
		teamIndex = client.getTeamIndex();
		teamHasAnswered = new HashMap<>();
		for (int i = 0; i < numTeams; i++)
			teamHasAnswered.put(i, false);
	}

	// returns whether every team has had a chance at answering this question
	public Boolean questionDone() {
		Boolean questionDone = true;
		for (Boolean currentTeam : teamHasAnswered.values())
			questionDone = questionDone && currentTeam;
		return questionDone;
	}

	public Timer getTimer() {
		return timer;
	}

	public Timer getClockTimer() {
		return clockTimer;
	}

	private void setClock() {
		clockTimer = new Timer(83, new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (images == null) {
					images = gui.images;
				}
				teamLabel.setIcon(images.get(i));
				i++;
				if (i == images.size() - 1) {
					i = 0;
				}
				if (!questionPanel.isVisible()) {
					((Timer) e.getSource()).stop();
				}
			}

		});
		clockTimer.start();
	}

	private void setFish() {
		clockTimer = new Timer(200, new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fishImages == null) {
					fishImages = gui.fishImages;
				}
				announcementsLabel.setIcon(fishImages.get(i));
				i++;
				if (i == fishImages.size() - 1) {
					i = 0;
				}
				if (!questionPanel.isVisible()) {
					((Timer) e.getSource()).stop();
				}
			}

		});
		clockTimer.start();
	}

	// overrides the addActionListeners method in super class
	@Override
	public void addActionListeners(MainGUI mainGUI, GameData gameData) {
		gui = ((MainGUINetworked) mainGUI);
		images = gui.images;
		passButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				startTimer(19);
				client.sendMessage(new BuzzInMessage(teamIndex));
			}

		});

		gameBoardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// send a question clicked message
				client.sendMessage(new QuestionClickedMessage(getX(), getY()));
			}
		});

		submitAnswerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				// send question answered message
				client.sendMessage(new QuestionAnsweredMessage(answerField.getText()));
				if (answerField.getText().equals("Too Late!")) {
					for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
						mainGUI.teams[i].setIcon(null);
						mainGUI.teams[i].revalidate();
					}
					answerField.setText("Please enter an answer.");
					setWaitBuzz(true);
				} else if (answerField.getText().equals("No one clicked!")) {
					submitAnswerButton.setEnabled(false);
					gameData.nextTurn();
					gui.addUpdate("The correct answer was: " + getAnswer());

					if (gameData.readyForFinalJeoaprdy()) {
						gui.startFinalJeopardy();
					}
					// if not ready, move on to another question
					else {
						// add update to Game Progress, determine whether the
						// game board
						// buttons should be enabled or disabled, and change the
						// main panel
						gui.addUpdate(
								"It is " + gameData.getCurrentTeam().getTeamName() + "'s turn to choose a question.");

						if (gameData.getCurrentTeam().getTeamIndex() != client.getTeamIndex())
							gui.disableAllButtons();
						for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
							mainGUI.teams[i].setIcon(null);
							mainGUI.teams[i].revalidate();
						}
						mainGUI.showMainPanel();
						gui.titleTimer();
					}
				}
			}

		});
	}

	// override the resetQuestion method
	@Override
	public void resetQuestion() {
		super.resetQuestion();
		hadSecondChance = false;
		currentTeam = null;
		originalTeam = null;
		teamHasAnswered.clear();
		// reset teamHasAnswered map so all teams get a chance to anaswer again
		for (int i = 0; i < numTeams; i++)
			teamHasAnswered.put(i, false);
	}

	@Override
	public void populate() {
		super.populate();
		passButton.setText("Buzz In!");
	}

	public int getOriginalTeam() {
		return originalTeam.getTeamIndex();
	}

	public void updateAnnouncements(String announcement) {
		announcementsLabel.setText(announcement);
	}

	public void setOriginalTeam(int team, GameData gameData) {
		originalTeam = gameData.getTeamDataList().get(team);
		updateTeam(team, gameData);
	}

	// update the current team of this question
	public void updateTeam(int team, GameData gameData) {
		currentTeam = gameData.getTeamDataList().get(team);
		passButton.setVisible(false);
		answerField.setText("");
		// if the current team is this client
		for (int i = 0; i < gameData.getNumberOfTeams(); ++i) {
			gui.teams[i].setIcon(null);
			gui.revalidate();
		}
		gui.clockOnTeam(gui.teams[team]);
		if (team == teamIndex) {

			teamLabel.setForeground(AppearanceConstants.darkBlue);
			setClock();
			AppearanceSettings.setEnabled(true, submitAnswerButton, answerField);
			announcementsLabel.setText("It's your turn to try to answer the question!");
			startTimer(19);
		}
		// if the current team is an opponent
		else {
			teamLabel.setForeground(Color.lightGray);
			teamLabel.setIcon(null);
			teamLabel.revalidate();
			setFish();
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
			announcementsLabel.setText("It's " + currentTeam.getTeamName() + "'s turn to try to answer the question.");
		}
		// mark down that this team has had a chance to answer
		teamHasAnswered.replace(team, true);
		hadSecondChance = false;
		teamLabel.setText(currentTeam.getTeamName());
	}

	// called from QuestionAnswerAction when there is an illformated answer
	public void illFormattedAnswer() {

		if (currentTeam.getTeamIndex() == teamIndex) {
			announcementsLabel.setText("You had an illformatted answer. Please try again");
		} else {
			announcementsLabel
					.setText(currentTeam.getTeamName() + " had an illformatted answer. They get to answer again.");
		}
	}

	// set the gui to be a buzz in period, also called from QuestionAnswerAction
	public void setBuzzInPeriod() {

		passButton.setVisible(true);
		teamLabel.setText("");
		teamLabel.setIcon(null);
		teamLabel.revalidate();
		announcementsLabel.setIcon(null);
		if (teamHasAnswered.get(teamIndex)) {
			timer.stop();
			startTimer(19);
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField, passButton);
			announcementsLabel.setText("It's time to buzz in! But you've already had your chance..");
		} else {
			timer.stop();
			startTimer(19);
			setWaitBuzz(true);
			announcementsLabel.setText("Buzz in to answer the question!");
			passButton.setEnabled(true);
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
		}
	}

	public TeamGUIComponents getCurrentTeam() {
		return currentTeam;
	}

}

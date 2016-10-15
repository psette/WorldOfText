package other_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import frames.MainGUI;
import frames.WinnersGUI;
import game_logic.GameData;
import game_logic.QuestionAnswer;
import game_logic.TeamData;
import listeners.TextFieldFocusListener;

public class QuestionGUIElement extends QuestionAnswer {
	// TODO PASS BUTTON, whoTurn needs to be implemented, good luck nigga, i
	// believe
	// the JButton displayed on the game board grid on MainGUI associated with
	// this question
	private JButton gameBoardButton;
	// the panel that will be switched out with the main panel when the
	// gameBoardButton is clicked
	private JPanel questionPanel;
	// the label that would show whether there was a format problem with the
	// submitted answer
	private JLabel announcementsLabel;
	// components for the questionPanel
	private JLabel categoryLabel;
	private JLabel pointLabel;
	private JLabel teamLabel;
	private JTextPane questionLabel;
	private JTextField answerField;
	private JButton submitAnswerButton, passButton;
	private Boolean hadSecondChance;
	private ImageIcon questionIcon;
	private int numPassed;

	public QuestionGUIElement(String question, String answer, String category, int pointValue, int indexX, int indexY,
			ImageIcon asked) {
		super(question, answer, category, pointValue, indexX, indexY);
		hadSecondChance = false;
		questionIcon = asked;
		numPassed = 1;
		initializeComponents();
		createGUI();
		addActionListeners();
	}

	// public methods
	public JButton getGameBoardButton() {
		return gameBoardButton;
	}

	// this method is called from the MainGUI; cannot add the action listeners
	// until then
	public void addActionListeners(MainGUI mainGUI, GameData gameData) {
		SubmitAnswerActionListener tempListen = new SubmitAnswerActionListener(mainGUI, gameData);
		submitAnswerButton.addActionListener(tempListen);
		gameBoardButton.addActionListener(new GameBoardActionListener(mainGUI, gameData));
		passButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numPassed++;
				tempListen.questionHasBeenAnswered(true);
			}
		});
	}

	// method used to reset the data of this question to it's default
	// called from MainGUI when user chooses 'Restart This Game' option on the
	// menu
	public void resetQuestion() {
		submitAnswerButton.setEnabled(true);
		asked = false;
		teamLabel.setText("");
		gameBoardButton.setEnabled(true);
		gameBoardButton.setBackground(Color.darkGray);
		answerField.setText("");
	}

	// private methods

	// initialize member variables
	private void initializeComponents() {
		questionPanel = new JPanel();
		passButton = new JButton("Pass");
		gameBoardButton = new JButton("$" + pointValue);
		pointLabel = new JLabel("$" + pointValue);
		categoryLabel = new JLabel(category);
		questionLabel = new JTextPane();
		announcementsLabel = new JLabel("");
		answerField = new JTextField("Enter your answer.");
		submitAnswerButton = new JButton("Submit Answer");
		teamLabel = new JLabel("");
		passButton.setEnabled(false);
	}

	private void createGUI() {

		// local variables
		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel answerPanel = new JPanel(new BorderLayout());
		JPanel formatErrorPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel temp = new JPanel();
		// appearance settings
		AppearanceSettings.setBackground(Color.darkGray, gameBoardButton, questionPanel, announcementsLabel,
				answerPanel, formatErrorPanel, temp);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, teamLabel, pointLabel, categoryLabel, infoPanel);
		AppearanceSettings.setForeground(Color.lightGray, gameBoardButton, teamLabel, pointLabel, categoryLabel,
				announcementsLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, questionLabel, teamLabel, pointLabel, categoryLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, gameBoardButton, announcementsLabel,
				submitAnswerButton, answerField);
		AppearanceSettings.setTextAlignment(teamLabel, pointLabel, categoryLabel, announcementsLabel);

		questionLabel.setText(question);
		questionLabel.setEditable(false);
		// sourced from:
		// http://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
		// centers the text in the question pane
		StyledDocument doc = questionLabel.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		gameBoardButton.setBorder(BorderFactory.createLineBorder(AppearanceConstants.darkBlue));
		gameBoardButton.setOpaque(true);
		answerField.setForeground(Color.gray);
		questionLabel.setBackground(AppearanceConstants.lightBlue);

		// components that need their size set
		questionLabel.setPreferredSize(new Dimension(800, 400));
		answerField.setPreferredSize(new Dimension(600, 100));
		infoPanel.setPreferredSize(new Dimension(900, 80));
		formatErrorPanel.setPreferredSize(new Dimension(800, 100));

		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		// add components to the panels
		infoPanel.add(teamLabel, BorderLayout.EAST);
		infoPanel.add(categoryLabel, BorderLayout.CENTER);
		infoPanel.add(pointLabel, BorderLayout.WEST);

		answerPanel.add(answerField, BorderLayout.CENTER);
		answerPanel.add(submitAnswerButton, BorderLayout.EAST);
		temp.add(passButton);
		answerPanel.add(temp, BorderLayout.SOUTH);

		formatErrorPanel.add(announcementsLabel);

		northPanel.add(infoPanel);
		northPanel.add(formatErrorPanel);

		questionPanel.add(northPanel, BorderLayout.NORTH);
		questionPanel.add(questionLabel, BorderLayout.CENTER);
		questionPanel.add(answerPanel, BorderLayout.SOUTH);

	}

	// add focus listener to answer text field, and the rest of the action
	// listeners will be added later from a call from MainGUI
	private void addActionListeners() {
		answerField.addFocusListener(new TextFieldFocusListener("Enter your answer", answerField));
	}

	// private listener classes
	// action listener for gameBoardButton
	private class GameBoardActionListener implements ActionListener {

		private MainGUI mainGUI;
		private GameData gameData;

		public GameBoardActionListener(MainGUI mainGUI, GameData gameData) {
			this.mainGUI = mainGUI;
			this.gameData = gameData;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// change panel to the question panel
			mainGUI.changePanel(questionPanel);
			// set the label of which team chose the question
			teamLabel.setText(gameData.getCurrentTeam().getTeamName());
		}

	}

	// action listener for submitAnswerButton
	public class SubmitAnswerActionListener implements ActionListener {

		private MainGUI mainGUI;
		private GameData gameData;
		private String update;
		private Boolean answered;
		private Boolean correct;
		private String nextTeam;

		public SubmitAnswerActionListener(MainGUI mainGUI, GameData gameData) {
			this.mainGUI = mainGUI;
			this.gameData = gameData;
			answered = false;
			correct = false;
			update = "";
		}

		// checks the answer taken from answerField and determines whether the
		// player gets a second chance to answer
		private void checkAnswer() {
			String givenAnswer = answerField.getText();
			TeamData team = gameData.getCurrentTeam();
			// valid format
			if (gameData.validAnswerFormat(givenAnswer)) {
				// team got the answer right
				if (givenAnswer.trim().toLowerCase().endsWith(answer.toLowerCase())) {
					team.addPoints(pointValue);
					update = team.getTeamName() + " got the answer right! $" + pointValue
							+ " will be added to their total. ";
					correct = true;
					gameData.getCurrentTeam().updatePointsLabel();

				}
				// team got the answer wrong
				else {
					team.deductPoints(pointValue);
					update = team.getTeamName() + " got the answer wrong! $" + pointValue
							+ " will be deducted from their total. ";
					mainGUI.addUpdate(update);
					gameData.getCurrentTeam().updatePointsLabel();
					answered = true;
					if (numPassed == gameData.getNumberOfTeams()) {
						questionHasBeenAnswered(false);
					} else {
						update = gameData.getNext() + ", you can steal!";
						passButton.setEnabled(true);
						answerField.setText("");
					}
				}
				// answer is true, meaning no second chance
				answered = true;
			}
			// valid format
			else {
				// if the user already had a second chance, deduct points
				if (hadSecondChance) {
					announcementsLabel.setText("Your answer is still formatted incorrect. $" + pointValue
							+ " will be deducted from your total.");
					team.deductPoints(pointValue);
					update = team.getTeamName() + ", your answer is still formatted incorrect. $" + pointValue
							+ " will be deducted from their total. ";
					gameData.getCurrentTeam().updatePointsLabel();
					mainGUI.addUpdate(update);
					answered = true;
					hadSecondChance = false;
					if (numPassed == gameData.getNumberOfTeams()) {
						questionHasBeenAnswered(false);
					} else {
						update = gameData.getNext() + ", you can steal!";
						passButton.setEnabled(true);
						answerField.setText("");
					}

				}
				// if user has not had second chance yet, so answered = false
				else {
					announcementsLabel.setText("Invalid format of your answer. Remember to pose it as a question");
					answerField.setText("");
					hadSecondChance = true;
					answered = false;
					update = team.getTeamName()
							+ " had a badly formatted answer. They will get a second chance to answer";
					if (passButton.isEnabled()) {
						passButton.setEnabled(false);
					}

				}
			}
			gameData.getCurrentTeam().updatePointsLabel();
			mainGUI.addUpdate(update);
		}

		// functionality for when the question has been answered
		public void questionHasBeenAnswered(boolean calledFromPass) {
			if (numPassed > gameData.getNumberOfTeams() || correct) {
				// update team point label on MainGUI
				if (numPassed > gameData.getNumberOfTeams()) {
					mainGUI.addUpdate("No one got the answer right! It was: " + answer);
				}
				gameBoardButton.setEnabled(false);
				gameBoardButton.setIcon(questionIcon);
				// increment the number of chosen questions
				gameData.updateNumberOfChosenQuestions();
				// have all the questions been asked? if so, time for final
				// jeopardy
				String team = gameData.getCurrentTeam().getTeamName();
				while (!team.equals(nextTeam)) {
					gameData.nextTurn();
					team = gameData.getCurrentTeam().getTeamName();
				}
				gameData.nextTurn();
				nextTeam = gameData.getCurrentTeam().getTeamName();
				if (gameData.readyForFinalJeoaprdy()) {
					// in case we are playing a Quick Play game, disable
					// remaining
					// buttons
					gameData.disableRemainingButtons();
					playFinalJeopardy();
				} else {
					// change who's turn it is
					mainGUI.addUpdate("It's " + nextTeam + " turn to choose!");
					// go back to main screen
					mainGUI.showMainPanel();
				}
			} else if (calledFromPass) { // pass case
				if (gameData.readyForFinalJeoaprdy()) {
					// in case we are playing a Quick Play game, disable
					// remaining
					// buttons
					gameData.disableRemainingButtons();
					playFinalJeopardy();
				} else {
					String team = gameData.getCurrentTeam().getTeamName();
					gameData.nextTurn();
					mainGUI.addUpdate(team + " passes, it is " + gameData.getCurrentTeam().getTeamName()
							+ "  turn to try and answer the question!");
					answerField.setText("");
				}
			} else if (answered) {
				numPassed++;
				gameData.nextTurn();
			}
		}

		// functionality to determine if the teams are eligible to play final
		// jeopardy
		private void playFinalJeopardy() {
			mainGUI.addUpdate("It's time for Final Jeopardy!");
			// figure out the teams that qualified for final jeopardy
			gameData.determineFinalists();
			// if there are no qualifying teams, pop up a WinnersGUI (showing no
			// one won)
			if (gameData.getFinalistsAndEliminatedTeams().getFinalists().size() == 0) {
				mainGUI.showMainPanel();
				new WinnersGUI(gameData).setVisible(true);
			}
			// if there are final teams, change the current panel to the final
			// jeopardy view
			else {
				mainGUI.changePanel(new FinalJeopardyGUI(gameData, mainGUI));
			}

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (numPassed == 1) {
				nextTeam = gameData.getCurrentTeam().getTeamName();
			}
			// check whether their answer was correct, and whether we should
			// navigate back to the main screen
			checkAnswer();
			// if it has been answered, check if the game has finished
			questionHasBeenAnswered(false);
		}

	}
}

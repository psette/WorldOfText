package other_gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game_logic.GameData;
import game_logic.TeamData;
import listeners.TextFieldFocusListener;

public class TeamGUIComponents extends TeamData {
	// members for the main screen
	private JLabel mainTotalPointsLabel;
	private JLabel mainTeamNameLabel;
	// members for the final jeopardy screen
	private JButton fjBetButton;
	private JButton submitFJAnswerButton;
	private JTextField fjAnswerTextField;
	private JSlider fjBetSlider;
	private JLabel fjBetLabel;
	private JLabel fjTeamNameLabel;

	public TeamGUIComponents(Integer team, int totalPoints, String teamName) {
		super(team, totalPoints, teamName);
		initializeComponents();
		createGUI();
		// addActionListeners() called from prepareForFinalJeopardy()
	}

	// public methods
	// called from MainGUI when the user click the 'Restart This Game' option on
	// the menu
	public void resetTeam() {
		fjBetLabel.setText("$1");
		fjBetSlider.setValue(1);
		fjBetButton.setEnabled(true);
		submitFJAnswerButton.setEnabled(false);
		totalPoints = 0;
		correctFJAnswer = false;
		updatePointsLabel();
	}

	public void updatePointsLabel() {
		mainTotalPointsLabel.setText("$" + totalPoints);
	}

	// called from FinalJeopardyGUI constructor
	public void prepareForFinalJeopardy(FinalJeopardyGUI gui, GameData gameData) {
		initializeBetSlider();
		addActionListeners(gui, gameData);
	}

	// GETTERS
	public JLabel getTotalPointsLabel() {
		return mainTotalPointsLabel;
	}

	public JButton getBetButton() {
		return fjBetButton;
	}

	public JButton getFJAnswerButton() {
		return submitFJAnswerButton;
	}

	public JTextField getFJAnswerTextField() {
		return fjAnswerTextField;
	}

	public JSlider getBetSlider() {
		return fjBetSlider;
	}

	public JLabel getBetLabel() {
		return fjBetLabel;
	}

	public JLabel getFJTeamNameLabel() {
		return fjTeamNameLabel;
	}

	public JLabel getMainTeamNameLabel() {
		return mainTeamNameLabel;
	}

	// private methods
	private void initializeComponents() {
		mainTotalPointsLabel = new JLabel("$" + Long.toString(totalPoints));
		fjBetButton = new JButton("Set Bet");
		submitFJAnswerButton = new JButton("Submit Answer");
		fjAnswerTextField = new JTextField(teamName + "'s answer");
		fjBetSlider = new JSlider();
		fjBetLabel = new JLabel("$1");
		fjTeamNameLabel = new JLabel(teamName + "'s bet");
		mainTeamNameLabel = new JLabel(teamName);
	}

	private void createGUI() {
		// set appearances of components
		AppearanceSettings.setBackground(Color.darkGray, mainTeamNameLabel, mainTotalPointsLabel, fjTeamNameLabel,
				fjBetLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, fjTeamNameLabel, fjBetButton, submitFJAnswerButton,
				fjBetSlider);
		AppearanceSettings.setForeground(Color.lightGray, mainTeamNameLabel, fjTeamNameLabel, fjBetLabel, fjBetSlider);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, mainTeamNameLabel, fjBetLabel);
		AppearanceSettings.setTextAlignment(mainTeamNameLabel, fjTeamNameLabel, fjBetLabel, mainTotalPointsLabel);

		mainTeamNameLabel.setBorder(null);
		mainTotalPointsLabel.setForeground(AppearanceConstants.lightBlue);
		mainTotalPointsLabel.setFont(AppearanceConstants.fontMedium);
		fjAnswerTextField.setForeground(Color.gray);

		mainTotalPointsLabel.setBorder(null);
		fjAnswerTextField.setFont(AppearanceConstants.fontSmallest);
		submitFJAnswerButton.setEnabled(false);
		AppearanceSettings.setSize(500, 90, fjBetSlider);

		// and that's it! The components are added to panels is MainGUI and
		// FinalJeopardyGUI
	}

	// set the bet amount, so disable slider and button
	private void setBet(int bet) {
		this.bet = bet;
		fjBetButton.setEnabled(false);
		fjBetSlider.setEnabled(false);
	}

	// slider has been moved, so updated fjBetLabel
	private void changeBet(int bet) {
		// if they moved the slider to 0, change it 1, since their bet can't be
		// 0
		if (bet == 0) {
			fjBetSlider.setValue(1);
			fjBetLabel.setText("$1");
		} else {
			fjBetLabel.setText("$" + bet);
		}

	}

	private void addActionListeners(FinalJeopardyGUI finalJeopardyGUI, GameData gameData) {
		// slider action listener
		fjBetSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changeBet(fjBetSlider.getValue());
			}
		});
		// "Set Bet" button action listener
		fjBetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setBet(fjBetSlider.getValue());
				finalJeopardyGUI.increaseNumberOfBets(teamName + " bet $" + bet);
			}

		});

		fjAnswerTextField.addFocusListener(new TextFieldFocusListener(teamName + "'s answer", fjAnswerTextField));

		submitFJAnswerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				submitFJAnswerButton.setEnabled(false);
				String answer = fjAnswerTextField.getText().trim().toLowerCase();

				if (gameData.validAnswerFormat(answer)) {

					if (answer.endsWith(gameData.getFinalJeopardyAnswer())) {
						correctFJAnswer = true;
					}
				}

				finalJeopardyGUI.increaseNumberOfAnswers();
			}

		});
	}

	// initialize the bet slider for this team
	private void initializeBetSlider() {
		// else, divide their total by 6 as the major tick spacing
		if (totalPoints > 20) {
			AppearanceSettings.setMinTickSliders(totalPoints / 18, fjBetSlider);
			AppearanceSettings.setSliders(0, totalPoints, 1, totalPoints / 6, AppearanceConstants.fontSmall,
					fjBetSlider);
		}
		// if their points total is small, the tick spacing should be small
		else {
			AppearanceSettings.setSliders(0, totalPoints, 1, 1, AppearanceConstants.fontSmall, fjBetSlider);
		}

	}
}

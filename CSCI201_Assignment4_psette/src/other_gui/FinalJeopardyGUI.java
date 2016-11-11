package other_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import frames.MainGUI;
import frames.WinnersAndRatingGUI;
import game_logic.GameData;

//JPanel that contains the final jeopardy gui elements and functionality
public class FinalJeopardyGUI extends JPanel {
	private static final long serialVersionUID = -2494560606771244013L;
	private JLabel jeopardyQuestion;
	private GameData gameData;
	private int numTeamsBet;
	private int numTeamsAnswered;
	private MainGUI mainGUI;

	public FinalJeopardyGUI(GameData gameData, MainGUI mainGUI) {

		this.gameData = gameData;
		this.mainGUI = mainGUI;
		numTeamsBet = 0;
		numTeamsAnswered = 0;

		initialize();
		createGUI();
	}

	// public methods
	// called every time a 'Set Bet' button is pressed
	public void increaseNumberOfBets(String update) {
		// increase the number of teams that have made their bet
		numTeamsBet++;
		mainGUI.addUpdate(update);

		if (allTeamsBet()) {
			mainGUI.addUpdate("All teams have bet! The Final Jeopardy question is: " + "\n"
					+ gameData.getFinalJeopardyQuestion());
			// display jeopardy question
			jeopardyQuestion.setText(gameData.getFinalJeopardyQuestion());
			// enabling all of the 'Submit Answer' buttons
			for (TeamGUIComponents team : gameData.getTeamDataList()) {
				team.getFJAnswerButton().setEnabled(true);
			}
		}
	}

	public void increaseNumberOfAnswers() {
		numTeamsAnswered++;
		// checks to see if all teams have answered the question
		if (allTeamsAnswered()) {
			mainGUI.addUpdate(
					"All teams have answered. The Final Jeopardy answer is: " + gameData.getFinalJeopardyAnswer());
			gameData.addOrDeductTeamBets(mainGUI);
			new WinnersAndRatingGUI(gameData).setVisible(true);
		}
	}

	// returns a boolean indicating whether all teams have made their bet
	public Boolean allTeamsBet() {
		return numTeamsBet == gameData.getFinalistsAndEliminatedTeams().getFinalists().size();
	}

	// returns a boolean indication whether all teams have answered the final
	// jeopardy question
	public Boolean allTeamsAnswered() {
		return numTeamsAnswered == gameData.getFinalistsAndEliminatedTeams().getFinalists().size();
	}

	// private methods
	// all other GUI components we will reference from the TeamGUIComponents
	// objects
	private void initialize() {
		jeopardyQuestion = new JLabel("Wait for it...");
	}

	private void createGUI() {
		setLayout(new GridLayout(gameData.getFinalistsAndEliminatedTeams().getFinalists().size() + 3, 1));
		// local variables
		JPanel answerPanel = new JPanel(new GridLayout(2, 2));
		JPanel questionPanel = new JPanel();
		JPanel titlePanel = new JPanel();

		JLabel titleLabel = new JLabel("Final Jeopardy Round");
		// setting appearance of components
		AppearanceSettings.setBackground(Color.darkGray, answerPanel, this);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, questionPanel, jeopardyQuestion);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, titlePanel, titleLabel);
		AppearanceSettings.setTextAlignment(jeopardyQuestion, titleLabel);
		// other appearance settings
		titleLabel.setForeground(Color.lightGray);
		titleLabel.setOpaque(true);
		titleLabel.setFont(AppearanceConstants.fontLarge);

		jeopardyQuestion.setForeground(Color.darkGray);
		jeopardyQuestion.setFont(AppearanceConstants.fontMedium);
		jeopardyQuestion.setPreferredSize(new Dimension(1000, 100));

		titlePanel.add(titleLabel);
		titlePanel.setPreferredSize(new Dimension(1000, 70));
		// add the panel with the FJ title
		add(titlePanel);
		// iterate over the final teams and add their gui components to the
		// panels
		for (int i = 0; i < gameData.getFinalistsAndEliminatedTeams().getFinalists().size(); i++) {

			JPanel teamAnswerPanel = new JPanel(new BorderLayout());
			JPanel teamBetPanel = new JPanel(new BorderLayout());

			JPanel betLabelAndButtonPanel = new JPanel(new GridLayout(1, 2));
			JPanel sliderPanel = new JPanel(new GridLayout(1, 2));

			TeamGUIComponents team = gameData.getFinalistsAndEliminatedTeams().getFinalists().get(i);
			// initialize the team's slider based on their total, and add action
			// listeners to its buttons
			team.prepareForFinalJeopardy(this, gameData);

			sliderPanel.setPreferredSize(new Dimension(800, 100));
			teamAnswerPanel.setPreferredSize(new Dimension(500, 60));
			AppearanceSettings.setBackground(Color.darkGray, betLabelAndButtonPanel, sliderPanel, teamAnswerPanel,
					teamBetPanel);

			// create/add this team's answer panel
			teamAnswerPanel.add(team.getFJAnswerTextField(), BorderLayout.CENTER);
			teamAnswerPanel.add(team.getFJAnswerButton(), BorderLayout.EAST);

			sliderPanel.add(team.getBetSlider());
			sliderPanel.add(betLabelAndButtonPanel);

			betLabelAndButtonPanel.add(team.getBetLabel());
			betLabelAndButtonPanel.add(team.getBetButton());

			teamBetPanel.add(team.getBetSlider(), BorderLayout.CENTER);
			teamBetPanel.add(betLabelAndButtonPanel, BorderLayout.EAST);
			teamBetPanel.add(team.getFJTeamNameLabel(), BorderLayout.WEST);

			answerPanel.add(teamAnswerPanel);
			// add this team's bet panel
			add(teamBetPanel);
		}

		questionPanel.add(jeopardyQuestion);
		// panel with the FJ question
		add(questionPanel);
		// panel with all the submit answer buttons and text fields
		add(answerPanel);
	}
}

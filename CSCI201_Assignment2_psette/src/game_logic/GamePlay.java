package game_logic;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import gui.FinalPage;
import gui.GameBoard;
import gui.MainFrame;
import gui.QuestionPage;

public class GamePlay extends GameData {
	private int numberOfTeams;
	private int whoseTurn = -1;
	private static JTextPane[] scores;
	// how many questions have been chosen
	private int numberOfChosenQuestions;
	// total points for each team, each TeamPoints object holds team index, team
	// points, and team name
	static List<TeamData> teamData;
	private GameBoard gb;
	private static QuestionPage qp;
	private static Set<String> unmodifiableSetAnswerVerbs;
	private static Set<String> unmodifiableSetAnswerNouns;

	// DOES NOT IMPLEMENT EXIT AND REPLAY LOGIC
	public GamePlay(String fileName, List<TeamData> incomingTeamData) throws Exception {
		super(fileName);
		numberOfTeams = incomingTeamData.size();
		Random rand = new Random();
		int firstTeam = rand.nextInt(numberOfTeams);
		whoseTurn = firstTeam;
		br = new BufferedReader(new InputStreamReader(System.in));
		// initialize private variables
		numberOfChosenQuestions = 0;
		teamData = incomingTeamData;
		scores = new JTextPane[numberOfTeams];
		initializeAnswerFormatSet();
		gb = new GameBoard(teamData, categoriesMap, pointValuesMapToIndex, numberOfTeams, questions);
	}

	public GameBoard getBoard() {
		return gb;
	}

	private void initializeAnswerFormatSet() {
		Set<String> nounsModifiableSet = new HashSet<>();
		Set<String> verbsModifiableSet = new HashSet<>();
		nounsModifiableSet.add("who");
		nounsModifiableSet.add("where");
		nounsModifiableSet.add("when");
		nounsModifiableSet.add("what");
		verbsModifiableSet.add("is");
		verbsModifiableSet.add("are");

		unmodifiableSetAnswerNouns = Collections.unmodifiableSet(nounsModifiableSet);
		unmodifiableSetAnswerVerbs = Collections.unmodifiableSet(verbsModifiableSet);
	}

	public String turn() {
		return "It is " + teamData.get(whoseTurn).getTeamName() + " 's turn! \n";
	}

	// increment whose turn it is
	public int nextTurn(int currentTurn) {

		return (currentTurn + 1) == numberOfTeams ? 0 : currentTurn + 1;
	}

	// check whether the answer is in the format of a question
	public boolean validAnswerFormat(String answer) {

		if (answer.length() < 1)
			return false;

		String[] splitAnswer = answer.trim().split("\\s+");

		if (splitAnswer.length < 2)
			return false;

		return unmodifiableSetAnswerVerbs.contains(splitAnswer[1].toLowerCase())
				&& unmodifiableSetAnswerNouns.contains(splitAnswer[0].toLowerCase());
	}

	public void resetData() {
		whoseTurn = -1;
		numberOfChosenQuestions = 0;
		// total points for each team, each TeamPoints object holds team index,
		// team points, and team name
		for (TeamData team : teamData) {
			team.setPoints(0);
		}

		for (int i = 0; i < 5; i++) {

			for (int j = 0; j < 5; j++) {
				questions[i][j].resetHasBeenAsked();
			}
		}
		updateScores();
	}

	public QuestionPage askQuestion(int x, int y, String category, int pointVal) {
		qp = new QuestionPage(teamData.get(whoseTurn).getTeamName(), category, pointVal, questions[x][y]);
		return qp;
	}

	public String answerQuestion(QuestionPage temp, int xIndex, int yIndex, String givenAnswer, int pointValue,
			boolean repeat) {
		TeamData team = teamData.get(whoseTurn);
		if (!repeat) {
			whoseTurn = nextTurn(whoseTurn);

		}
		String expectedAnswer = questions[xIndex][yIndex].getAnswer();

		if (!validAnswerFormat(givenAnswer.trim())) {

			// if still not valid format, mark question as incorrect
			if (team.warned) {
				team.deductPoints(pointValue);
				return "Invalid question format." + pointValue
						+ " will be deducted from your total.The expected answer was: " + expectedAnswer;
			}
			temp.errorMsg.setVisible(true);
			team.warned = true;
			return "repeat";
		}

		// if we have not already deducted points because of format, actually
		// check validity of their answer

		if (!givenAnswer.toLowerCase().endsWith(expectedAnswer.toLowerCase())) {
			team.deductPoints(pointValue);
			return "You got the answer wrong! " + pointValue
					+ " will be deducted from your total.The expected answer was: " + expectedAnswer;
		} else {
			team.addPoints(pointValue);
			return "You got the answer right! " + pointValue + " will be added to your total. ";
		}
	}

	private List<TeamData> getFinalists() {
		List<TeamData> finalTeams = new ArrayList<>();

		for (int i = 0; i < teamData.size(); i++) {
			TeamData team = teamData.get(i);
			if (team.getPoints() > 0) {
				finalTeams.add(team);
			}
		}
		return finalTeams;
	}

	public JTextPane[] updateScores() {
		for (TeamData team : teamData) {
			scores[team.getTeam()].setText(team.getTeamName() + ": $" + team.getPoints());
		}
		return scores;
	}

	public JTextPane[] addScores() {
		for (TeamData team : teamData) {
			JTextPane temp = new JTextPane();
			temp.setText(team.getTeamName() + ": $" + team.getPoints());
			temp.setFont(new Font("SanSerif", 0, 18));
			temp.setEditable(false);
			temp.setOpaque(true);
			temp.setBackground(Color.gray);
			temp.setForeground(Color.WHITE);
			scores[team.getTeam()] = temp;
		}
		return updateScores();
	}

	public FinalPage playFinalJeopardy(JTextPane updates, MainFrame mainFrameIn) {
		// get the finalists for the round
		List<TeamData> finalTeams = getFinalists();
		FinalPage frame = new FinalPage((ArrayList<TeamData>) finalTeams, updates, finalJeopardyQuestion,
				finalJeopardyAnswer, this, mainFrameIn);
		return frame;

	}

	public boolean finalCorrect(String answer) {
		return !validAnswerFormat(answer.trim()) ? false
				: answer.toLowerCase().endsWith(finalJeopardyAnswer.toLowerCase());
	}

	public ArrayList<String> getWinners(List<TeamData> finalTeams) {
		// sorts the finalists in order of their total score
		Collections.sort(finalTeams, TeamData.getComparator());
		ArrayList<String> winners = new ArrayList<>();

		// the team at the end of the list must have the highest score and is
		// definitely a winner
		TeamData definiteWinnerObject = finalTeams.get(finalTeams.size() - 1);
		String definiteWinner = definiteWinnerObject.getTeamName();
		long max = definiteWinnerObject.getPoints();
		// if the max score is 0, we know that no one won
		if (max == 0)
			return winners;

		winners.add(definiteWinner);

		// check to see if there are other winners
		if (finalTeams.size() > 1) {

			for (int i = finalTeams.size() - 2; i > -1; i--) {

				if (finalTeams.get(i).getPoints() == max) {
					winners.add(finalTeams.get(i).getTeamName());
				}
			}
		}

		return winners;
	}

}

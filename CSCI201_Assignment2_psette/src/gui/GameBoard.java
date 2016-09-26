package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import game_logic.GameData.Category;
import game_logic.QuestionAnswer;
import game_logic.TeamData;

public class GameBoard extends JPanel {
	public static final long serialVersionUID = 1;
	protected static QuestionAnswer[][] questions;
	private static JButton[][] question = new JButton[5][5];
	protected Map<Integer, Integer> dollarAmmounts;
	protected Map<String, Category> categories;
	private static List<TeamData> teamData;
	private static Map<String, Category> categoriesMap;
	private static Map<Integer, Integer> pointValuesMapToIndex;
	public static int[] pointValues = new int[5];
	public static String[] categoriesStr = new String[5];

	public GameBoard(List<TeamData> teamDataIn, Map<String, Category> categoriesMapIn,
			Map<Integer, Integer> pointValuesMapToIndexIn, int numTeamsIn, QuestionAnswer[][] questionsIn) {
		super();
		pointValuesMapToIndex = pointValuesMapToIndexIn;
		for (Integer key : pointValuesMapToIndex.keySet()) {
			pointValues[pointValuesMapToIndexIn.get(key)] = key;
		}
		Arrays.sort(pointValues);
		teamData = teamDataIn;
		categoriesMap = categoriesMapIn;
		questions = questionsIn;
		setLayout(new BorderLayout());
		add(addTitle(), BorderLayout.NORTH);
		add(addQuestion(), BorderLayout.CENTER);
	}

	private static JPanel addTitle() {
		JPanel title = new JPanel();
		title.setOpaque(true);
		title.setBackground(Color.cyan);
		JLabel WelcomeLabel = new JLabel("Jeopardy", SwingConstants.CENTER);
		WelcomeLabel.setFont(new Font("SanSerif", 1, 24));
		title.add(WelcomeLabel);
		return title;
	}

	public QuestionAnswer[][] getQuestions() {
		return questions;
	}

	public JButton[][] getQuestionButton() {
		return question;
	}

	public int getIndexX(int category) {
		return categoriesMap.get(categoriesStr[category]).getIndex();
	}

	public int getIndexY(int pointValue) {
		return pointValuesMapToIndex.get(pointValues[pointValue]);
	}

	private static JPanel addQuestion() {
		JPanel questionPanel = new JPanel();
		questionPanel.setLayout(new GridLayout(6, 5));
		JLabel[] categoriesArr = new JLabel[5];
		Set<String> keySet = categoriesMap.keySet();
		int count = 0;
		for (String key : keySet) {
			categoriesStr[count++] = key;
			int index = categoriesMap.get(key).getIndex();
			categoriesArr[index] = new JLabel(categoriesMap.get(key).getCategory(), SwingConstants.CENTER);
			categoriesArr[index].setForeground(Color.WHITE);
			categoriesArr[index].setOpaque(true);
			categoriesArr[index].setBackground(Color.gray);
			categoriesArr[index].setBorder(BorderFactory.createLineBorder(Color.BLUE));
			questionPanel.add(categoriesArr[index]);
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				question[i][j] = new JButton(Integer.toString(pointValues[i]));
				question[i][j].setForeground(Color.WHITE);
				question[i][j].setOpaque(true);
				question[i][j].setBackground(Color.gray);
				question[i][j].setBorder(BorderFactory.createLineBorder(Color.BLUE));
				questionPanel.add(question[i][j]);
			}
		}
		return questionPanel;
	}
}

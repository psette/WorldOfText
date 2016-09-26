package game_logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameData {

	protected BufferedReader br;
	private FileReader fr;
	// contains questions with their answer and boolean flag as to whether they
	// have been asked
	protected QuestionAnswer[][] questions;

	// maps from the point value/category to their index in the appropriate
	// array
	protected Map<Integer, Integer> pointValuesMapToIndex;
	protected Map<String, Category> categoriesMap;

	protected String finalJeopardyQuestion;
	protected String finalJeopardyAnswer;

	public GameData(String fileName) throws Exception {

		pointValuesMapToIndex = new HashMap<>();
		categoriesMap = new HashMap<>();
		questions = new QuestionAnswer[5][5];

		fr = new FileReader(fileName);
		br = new BufferedReader(fr);

		parseCategoriesAndPoints();
		parseQuestions();
		close();
	}

	public void close() throws IOException {

		if (fr != null)
			fr.close();
		if (br != null)
			br.close();
	}

	public void parseCategoriesAndPoints() throws Exception {

		String categories = br.readLine();
		String[] parsedCategories = categories.split("::");

		if (parsedCategories.length != 5) {
			throw new Exception("Too many or too few categories provided.");
		}

		for (String str : parsedCategories) {

			if (str.trim().equals("")) {
				throw new Exception("One of the categories is whitespace.");
			}
		}

		String pointValues = br.readLine();
		String[] parsedPointValues = pointValues.split("::");

		if (parsedPointValues.length != 5) {
			throw new Exception("Too many or too few dollar values provided.");
		}

		for (int i = 0; i < 5; i++) {
			categoriesMap.put(parsedCategories[i].toLowerCase().trim(), new Category(parsedCategories[i].trim(), i));

			try {
				pointValuesMapToIndex.put(Integer.parseInt(parsedPointValues[i].trim()), i);
			} catch (NumberFormatException nfe) {
				throw new Exception("One of the point values is a string.");
			}
		}
	}

	public void parseQuestions() throws Exception {

		String templine = "";
		String fullData = "";
		int questionCount = 0;
		boolean haveFinalJeopardy = false;

		while (questionCount != 26) {

			templine = br.readLine();
			if (templine == null) {
				throw new Exception("Not enough questions in the file");
			}

			if (!templine.startsWith("::")) {
				fullData += templine;
			} else {

				// parsePrevious question
				if (questionCount != 0) {
					haveFinalJeopardy = parseQuestions(fullData, haveFinalJeopardy);
				}

				fullData = templine.substring(2);
				questionCount++;
			}

		}

		haveFinalJeopardy = parseQuestions(fullData, haveFinalJeopardy);

		if (br.readLine() != null) {
			throw new Exception("Two many questions provided.");
		}

		if (!haveFinalJeopardy) {
			throw new Exception("This game file does not have a final jeopardy question.");
		}
	}

	private Boolean parseQuestions(String line, Boolean haveFinalJeopardy) throws Exception {

		Boolean finalJeopardy = haveFinalJeopardy;

		if (line.toLowerCase().startsWith("fj")) {

			if (haveFinalJeopardy) {
				throw new Exception("Cannot have more than one final jeopardy question.");
			} else {

				parseFinalJeopardy(line);
				finalJeopardy = true;
			}

		} else {
			parseQuestionString(line);
		}
		return finalJeopardy;
	}

	private void parseFinalJeopardy(String finalJeopardyString) throws Exception {

		String[] questionData = finalJeopardyString.split("::");

		if (questionData.length != 3)
			throw new Exception("Too much or not enough data provided for the final jeopardy question.");

		if (questionData[1].trim().equals(""))
			throw new Exception("The Final Jeopardy question cannot be whitespace");

		if (questionData[2].trim().equals(""))
			throw new Exception("The Final Jeopardy answer cannot be whitespace");

		finalJeopardyQuestion = questionData[1].trim();
		finalJeopardyAnswer = questionData[2].trim();

	}

	// does not check whether there is a duplicate category/point value question
	private void parseQuestionString(String question) throws Exception {

		String[] questionData = question.split("::");

		if (questionData.length != 4) {
			throw new Exception("Too much or not enough data provided for a question");
		} else {

			String category = questionData[0].trim();

			if (!categoriesMap.containsKey(category.toLowerCase()))
				throw new Exception("This category does not exist: " + category);

			Integer pointValue = -1;

			pointValue = Integer.parseInt(questionData[1].trim());

			if (!pointValuesMapToIndex.containsKey(pointValue))
				throw new Exception("This point value does not exist: " + pointValue);

			int indexX = categoriesMap.get(category.toLowerCase().trim()).getIndex();
			int indexY = pointValuesMapToIndex.get(pointValue);

			if (questionData[2].trim().equals(""))
				throw new Exception("The question cannot be whitespace.");

			if (questionData[3].trim().equals(""))
				throw new Exception("The answer cannot be whitespace.");

			questions[indexX][indexY] = new QuestionAnswer(questionData[2].trim(), questionData[3].trim());
		}
	}

	public class Category {

		private String category;
		private int index;

		public Category(String category, int index) {
			this.category = category;
			this.index = index;
		}

		public String getCategory() {
			return category;
		}

		public int getIndex() {
			return index;
		}
	}
}

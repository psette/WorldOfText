package game_logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import other_gui.QuestionGUIElementNetworked;
import other_gui.TeamGUIComponentsNetworked;
import server.ServerToClientThread;

//inherits from GameData
public class ServerGameData extends GameData implements Serializable {
	private static final long serialVersionUID = 2848299414794132376L;

	private ArrayList<BufferedImage> images;

	// its only member variable that is not inherited
	private QuestionGUIElementNetworked[][] questionData;

	public ServerGameData() {
		super();
		questionData = new QuestionGUIElementNetworked[5][5];
	}

	// set the teams by getting the team name from each of the serverThreads
	public void setTeams(ServerToClientThread[] threads) {
		for (int i = 0; i < threads.length; i++) {
			teamDataList.add(new TeamGUIComponentsNetworked(i, 0, threads[i].getTeamName()));
		}
	}

	// override clear data so we can also clear the networked questions
	@Override
	public void clearData() {
		super.clearData();
		questionData = new QuestionGUIElementNetworked[5][5];
	}

	// different nextTurn method which doesn't set whoseTurn, just returns the
	// index of the team that would
	// be next in clockwise order
	public int nextTurn(int currentTurn) {
		return ((currentTurn + 1) == numberOfTeams) ? 0 : currentTurn + 1;
	}

	public int getAverageRating() {
		return averageRating;
	}

	// gui variables are transient, so we need to repopulate them on the client
	// side
	public void populate() {

		teamDataList.stream().forEach(team -> team.populate());
		categoriesMap.values().stream().forEach(category -> category.populate());

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				QuestionGUIElementNetworked question = questionData[i][j];
				question.populate();
			}
		}

	}

	public void readAnimations() {
		images = new ArrayList<BufferedImage>();
		File dir = new File("clockAnimation");
		if (dir.isDirectory()) { // make sure it's a directory
			File[] list = dir.listFiles();
			System.out.println(list.length);
			for (File f : list) {
				if (f.getName().endsWith("gif")) {
					continue;
				}
				// System.out.println(f.getName());
				@SuppressWarnings("unused")
				BufferedImage img = null;
				try {
					images.add(ImageIO.read(f));
				} catch (Exception e) {
					System.out.println("Check clockAnimation directory");
				}
			}
		}
	}

	// override the addQuestion method which is called in the GameData class
	// when it is parsing the file
	// this allows us to store the question in our 2D array as well as the
	// hashmap
	@Override
	protected void addQuestion(String question, String answer, String category, int pointValue, int indexX,
			int indexY) {
		QuestionGUIElementNetworked newQ = new QuestionGUIElementNetworked(question, answer, category, pointValue,
				indexX, indexY);
		questions.add(newQ);
		questionData[indexX][indexY] = newQ;
	}

	// returns the 2D array of networked questions
	public QuestionGUIElementNetworked[][] getNetworkedQuestions() {
		return questionData;
	}

}

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import frames.MainGUI;
import frames.StartWindowGUI;
import frames.WinnersAndRatingGUI;
import game_logic.GameData;
import network_messages.AddGameData;
import network_messages.AddUpdate;
import network_messages.ChangePanel;
import network_messages.ChangeToFinal;
import network_messages.CorrectAnswer;
import network_messages.NextTurn;
import network_messages.UpdateNumberOfChosenQuestions;
import network_messages.WinnersAndRating;
import network_messages.setDisabeledQuestion;
import other_gui.QuestionGUIElement;

public class GameServer extends Thread {

	private Vector<ServerThread> serverThreads;
	int count = 0;
	GameData data;
	private ArrayList<String> teams = null;
	private StartWindowGUI startWindowGUI = null;
	private int numTeams = 0;
	private ServerSocket ss = null;
	private boolean started = false;

	public GameServer(int port, int numTeams, StartWindowGUI startWindow, String teamName) {
		this.numTeams = numTeams;
		this.setStartWindowGUI(startWindow);
		teams = new ArrayList<String>();
		teams.add(teamName);
		serverThreads = new Vector<ServerThread>();
		try {
			ss = new ServerSocket(port);
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
		startWindowGUI.setWaiting("Waiting for 3 other players to join...");
		this.start();
	}

	public void run() {
		System.out.println("Looping");
		while (!started) {
			UpdateLoginGUI();
			if (started) {
				break;
			}
			Socket s;
			try {
				s = ss.accept();
				System.out.println("connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		startMain();
	}

	private void UpdateLoginGUI() {
		System.out.println(teams.size() + " " + numTeams);
		switch (numTeams - teams.size()) {
		case 3:
			startWindowGUI.setWaiting("Waiting for 3 other players to join...");
			break;
		case 2:
			startWindowGUI.setWaiting("Waiting for 2 other players to join...");
			break;
		case 1:
			startWindowGUI.setWaiting("Waiting for 1 more player...");
			break;
		case 0:
			startWindowGUI.setWaiting("Game starting");
			started = true;
			break;
		}

	}

	private void startMain() {
		data.setTeams(teams, numTeams);
		for (ServerThread st : serverThreads) {
			st.sendMessage(new AddGameData(data));
		}
		startWindowGUI.dispose();
	}

	public void addData(GameData data) {
		this.data = data;
	}

	public boolean addTeam(String teamName) {
		for (int i = 0; i < teams.size(); ++i) {
			if (teams.get(i).equals(teamName)) {
				return false;
			}
		}
		System.out.println("team added" + teamName);
		teams.add(teamName);
		this.run();
		return true;
	}

	public StartWindowGUI getStartWindowGUI() {
		return startWindowGUI;
	}

	public void setStartWindowGUI(StartWindowGUI startWindowGUI) {
		this.startWindowGUI = startWindowGUI;
	}

	public void nextTurn(ServerThread serverThread) {
		for (ServerThread thread : serverThreads) {
			if (thread.equals(serverThread)) {
				continue;
			} else {
				thread.sendMessage(new NextTurn());
			}
		}
	}

	public void updateText(String update, ServerThread serverThread) {
		for (ServerThread thread : serverThreads) {
			if (thread.equals(serverThread)) {
				continue;
			} else {
				thread.sendMessage(new AddUpdate(update));
			}
		}

	}

	public void correctResponse(int pointValue, String team, ServerThread serverThread) {
		for (int i = 0; i < data.getNumberOfTeams(); i++) {
			if (data.getTeamDataList().get(i).getTeamName().equals(team)) {
				for (ServerThread thread : serverThreads) {
					if (thread.equals(serverThread)) {
						continue;
					} else {
						thread.sendMessage(new CorrectAnswer(pointValue, team));
						thread.sendMessage(new UpdateNumberOfChosenQuestions());
					}
				}
				return;
			}
		}

	}

	public void showFinalJeopardy(GameData data, MainGUI mainGUI) {
		for (ServerThread thread : serverThreads) {
			thread.sendMessage(new ChangeToFinal(true, data, mainGUI));
		}

	}

	public void showQuestionPage(GameData data, MainGUI mainGUI) {
		for (ServerThread thread : serverThreads) {
			thread.sendMessage(new ChangeToFinal(false, data, mainGUI));
		}

	}

	public void UpdateNumQuest(ServerThread serverThread) {
		for (ServerThread thread : serverThreads) {
			if (thread.equals(serverThread)) {
				continue;
			} else {
				thread.sendMessage(new UpdateNumberOfChosenQuestions());
			}
		}

	}

	public void winnersGUI(WinnersAndRatingGUI gui) {
		for (ServerThread thread : serverThreads) {
			thread.sendMessage(new WinnersAndRating(gui));
		}
	}

	public void setDisabledQuestion(boolean setDisabeled, int x, int y, ServerThread serverThread) {
		for (ServerThread thread : serverThreads) {
			if (thread.equals(serverThread)) {
				continue;
			} else {
				thread.sendMessage(new setDisabeledQuestion(setDisabeled, x, y));
			}
		}

	}

	public void showPanel(QuestionGUIElement element, ServerThread serverThread) {
		for (ServerThread thread : serverThreads) {
			if (thread.equals(serverThread)) {
				continue;
			} else {
				thread.sendMessage(new ChangePanel(element));
			}
		}

	}

}

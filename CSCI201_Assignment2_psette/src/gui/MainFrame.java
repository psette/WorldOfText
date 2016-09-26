package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import game_logic.GamePlay;
import game_logic.QuestionAnswer;
import game_logic.TeamData;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1;
	private JPanel cards;
	private String file;
	private ArrayList<TeamData> teamData;
	private GamePlay gp;
	private MainFrame mf;
	private GameBoard gb;
	private JTextPane updates = new JTextPane();
	private int numAsked = 0;
	private boolean quickPlay;
	private JMenu controller = new JMenu("Menu");
	public JMenuItem newFile = controller.add("Choose New Game File");

	public MainFrame(String inFile, ArrayList<TeamData> inTeamData, boolean quickPlayIn) {
		mf = this;
		file = inFile;
		teamData = inTeamData;
		quickPlay = quickPlayIn;
		instantiateComponents();
		setSize(1500, 1000);
		setLocation(0, 0);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		createMenu();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(null, "Are You Sure?", null, JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					System.exit(0);
				}
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});
	}

	public JPanel addInfo() {
		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		JPanel GameProg = new JPanel();
		JLabel gptext = new JLabel("Game Progress");
		gptext.setFont(new Font("SanSerif", 1, 24));
		GameProg.add(gptext);
		GameProg.setOpaque(true);
		GameProg.setBackground(Color.CYAN);
		GameProg.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		updates.setEditable(false);
		updates.setOpaque(true);
		updates.setBackground(Color.CYAN);
		updates.setText(gp.turn());
		JScrollPane scroll = new JScrollPane(updates);
		JTextPane[] scores = gp.addScores();
		for (TeamData team : teamData) {
			info.add(scores[team.getTeam()]);
		}
		scroll.setPreferredSize(new Dimension(250, 300));
		info.add(GameProg);
		info.add(scroll);
		info.setPreferredSize(new Dimension(250, 700));
		return info;
	}

	@SuppressWarnings("static-access")
	private void instantiateComponents() {
		try {
			gp = new GamePlay(file, teamData);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		gb = gp.getBoard();
		cards = new JPanel();
		setLayout(new BorderLayout());
		cards.setLayout(new CardLayout());
		cards.add(gb, "GamePlay");
		add(addInfo(), BorderLayout.EAST);
		add(cards);
		JButton[][] questionButton = gb.getQuestionButton();
		QuestionAnswer[][] questions = gb.getQuestions();
		QuestionPage[][] questionPages = new QuestionPage[5][5];
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				int category = gb.getIndexX(j), pointVal = gb.getIndexY(i);
				QuestionPage temp = gp.askQuestion(category, pointVal, gb.categoriesStr[j], gb.pointValues[pointVal]);
				questionPages[category][pointVal] = temp;
				cards.add(temp, questions[category][pointVal].getQuestion());
				questionButton[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((CardLayout) cards.getLayout()).show(cards, questions[category][pointVal].getQuestion());
					}
				});
				int xIndex = i, yIndex = j;
				temp.submitButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String msg = gp.answerQuestion(temp, category, pointVal, temp.blank_field.getText(),
								gb.pointValues[pointVal], false);
						if (msg.equals("repeat")) {
							msg = gp.answerQuestion(temp, category, pointVal, temp.blank_field.getText(),
									gb.pointValues[pointVal], true);
						} else {
							updates.setText(updates.getText() + "\n" + msg);
							((CardLayout) cards.getLayout()).show(cards, "GamePlay");
							questionButton[xIndex][yIndex].setBackground(Color.black);
							questionButton[xIndex][yIndex].setText("");
							questionButton[xIndex][yIndex].setEnabled(false);
							++numAsked;
							gp.updateScores();
							if (numAsked >= 5 && quickPlay) {
								playFinalJeopardy();
							} else if (numAsked >= 25) {
								playFinalJeopardy();
							}
						}
					}
				});
			}
		}
	}

	private void playFinalJeopardy() {
		FinalPage fp = gp.playFinalJeopardy(updates, this);
		if (fp.teams.size() == 0) {
			int result = JOptionPane.showConfirmDialog((Component) null, "No one wins. Would you like to replay?",
					"alert", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				restart();
			} else {
				System.exit(0);
			}
		} else {
			cards.add(fp, "Final");
			((CardLayout) cards.getLayout()).show(cards, "Final");

		}
	}

	public void restart() {
		mf.dispose();
		mf = new MainFrame(file, teamData, quickPlay);
		gp.resetData();
	}

	private void createMenu() {
		JMenuBar menu = new JMenuBar();
		JMenuItem restart = controller.add("Restart This Game"), exit = controller.add("Exit Game");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});
		menu.add(controller);
		setJMenuBar(menu);
	}

}
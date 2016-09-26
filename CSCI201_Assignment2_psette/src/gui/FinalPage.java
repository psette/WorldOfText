package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import game_logic.GamePlay;
import game_logic.TeamData;

public class FinalPage extends JPanel {
	public static final long serialVersionUID = 1;
	public JTextField[] blankFields;
	public ArrayList<TeamData> teams;
	private JSlider[] sliders;
	private JButton[] buttons, ansButtons;
	private JTextArea question = new JTextArea();
	private int[] bets;
	private JTextPane updates;
	private String finalQuestion;
	private GamePlay gp;
	private ArrayList<String> winners;
	public String winningString = "And the winner is...\n";
	private MainFrame mainFrame;

	public FinalPage(ArrayList<TeamData> teamsIn, JTextPane updatesIn, String finalQuestionIn,
			String finalJeopardyAnswer, GamePlay gpIn, MainFrame mainFrameIn) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		updates = updatesIn;
		teams = teamsIn;
		bets = new int[teams.size()];
		blankFields = new JTextField[teams.size()];
		buttons = new JButton[teams.size()];
		ansButtons = new JButton[teams.size()];
		sliders = new JSlider[teams.size()];
		finalQuestion = finalQuestionIn;
		gp = gpIn;
		mainFrame = mainFrameIn;
		question.setEditable(false);
		question.setSize(new Dimension(300, 540));
		question.setMaximumSize(new Dimension(300, 540));
		question.setMinimumSize(new Dimension(300, 540));
		add(addTop());
		add(addSliders());
		add(addQuestion());
		add(addAnswers());
		setBackground(Color.blue);
	}

	private JPanel addAnswers() {
		JPanel main = new JPanel();
		main.setLayout(new GridLayout(2, 2));
		for (int i = 0; i < teams.size(); ++i) {
			JPanel temp = new JPanel();
			temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
			blankFields[i] = new JTextField(20);
			int iCopy = i;
			blankFields[iCopy].addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (!sliders[iCopy].isEnabled()) {
						ansButtons[iCopy].setEnabled(true);
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}
			});
			ansButtons[i] = new JButton("Submit answer");
			ansButtons[i].setEnabled(false);
			temp.add(blankFields[i]);
			temp.add(ansButtons[i]);
			main.add(temp);
		}
		return main;
	}

	private JPanel addQuestion() {
		JPanel main = new JPanel();
		question.setText("And the question is...");
		question.setFont(new Font("SanSerif", 1, 24));
		question.setBackground(Color.BLUE);
		question.setForeground(Color.WHITE);
		main.setBackground(Color.blue);
		question.setLineWrap(true);
		question.setWrapStyleWord(true);
		main.add(question);
		return main;
	}

	private JPanel addSliders() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		for (int i = 0; i < teams.size(); ++i) {
			JPanel temp = new JPanel();
			temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
			int bet = teams.get(i).getPoints().intValue();
			JSlider slider = new JSlider(0, bet);
			slider.setFont(new Font("Calibri", Font.BOLD, 10));
			slider.setMajorTickSpacing(bet / 10);
			slider.setPaintLabels(true);
			slider.setPaintTicks(true);
			slider.setPaintTrack(true);
			slider.setAutoscrolls(true);
			slider.setForeground(Color.WHITE);
			slider.setBackground(Color.gray);
			slider.setOpaque(true);
			slider.setValue(0);
			sliders[i] = slider;
			JLabel teaminfo = new JLabel(teams.get(i).getTeamName() + "'s bet.");
			teaminfo.setForeground(Color.white);
			temp.add(teaminfo);
			temp.add(sliders[i]);
			JLabel betInfo = new JLabel("$ 0");
			betInfo.setForeground(Color.white);
			int iCopy = i;
			sliders[i].addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					betInfo.setText("$ " + sliders[iCopy].getValue());
				}
			});
			temp.add(betInfo);
			buttons[i] = new JButton("Set Bet");
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					bets[iCopy] = sliders[iCopy].getValue();
					buttons[iCopy].setEnabled(false);
					sliders[iCopy].setEnabled(false);
					updates.setText(updates.getText() + "\n" + teams.get(iCopy).getTeamName() + " bets $" + bets[iCopy]
							+ ".\n");
					boolean show = true;
					for (int i = 0; i < teams.size(); ++i) {
						if (buttons[i].isEnabled()) {
							show = false;
							break;
						}
					}
					if (show) {
						ask();
					}
				}
			});
			temp.add(buttons[i]);
			temp.setBackground(Color.gray);
			temp.setOpaque(true);
			main.add(temp);
		}
		main.setBackground(Color.gray);
		main.setOpaque(true);
		return main;
	}

	private void ask() {
		question.setText(finalQuestion);
		for (int i = 0; i < teams.size(); ++i) {
			int iCopy = i;
			ansButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					blankFields[iCopy].setEditable(false);
					ansButtons[iCopy].setEnabled(false);
					if (!gp.finalCorrect(blankFields[iCopy].getText())) {
						teams.get(iCopy).deductPoints(bets[iCopy]);
						updates.setText(updates.getText() + "\n" + teams.get(iCopy).getTeamName()
								+ " is incorrect, final score is: $" + teams.get(iCopy).getPoints() + ".\n");
					} else {
						teams.get(iCopy).addPoints(bets[iCopy]);
						updates.setText(updates.getText() + "\n" + teams.get(iCopy).getTeamName()
								+ " is correct, final score is: $" + teams.get(iCopy).getPoints() + ".\n");
					}
					boolean allDone = true;
					for (int j = 0; j < teams.size(); ++j) {
						if (blankFields[j].isEditable()) {
							allDone = false;
							break;
						}
					}
					if (allDone) {
						getWinners();
					}
				}

			});
		}

	}

	public void getWinners() {
		winners = gp.getWinners(teams);
		if (winners.size() == 1) {
			winningString += winners.get(0) + "!";
		} else if (winners.size() == 2) {
			winningString += winners.get(0) + " and " + winners.get(1) + "!";
		} else if (winners.size() == 3) {
			winningString += winners.get(0) + " ," + winners.get(1) + " and " + winners.get(2) + "!";
		} else {
			winningString += winners.get(0) + " ," + winners.get(1) + " ," + winners.get(2) + " , and " + winners.get(3)
					+ "!";
		}
		int result = JOptionPane.showConfirmDialog((Component) null, winningString + "\nWould you like to replay?",
				"alert", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			mainFrame.restart();
		} else {
			System.exit(0);
		}
	}

	private JLabel addTop() {
		JLabel title = new JLabel("Final Jeopardy");
		title.setOpaque(true);
		title.setFont(new Font("SanSerif", 1, 24));
		title.setBackground(Color.BLUE);
		title.setForeground(Color.WHITE);
		return title;
	}
}
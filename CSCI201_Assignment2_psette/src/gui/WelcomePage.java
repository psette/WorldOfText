package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import game_logic.GameData;

public class WelcomePage extends JFrame {
	public static final long serialVersionUID = 1;
	public boolean quick = false;
	private static JButton start = new JButton("Start Jeopardy");
	private static ArrayList<JPanel> panelList = new ArrayList<JPanel>(4);
	private static ArrayList<String> teamNames = new ArrayList<String>(4);
	private static int numTeams = 1;
	private static String file = "";
	private WelcomePage welcomepage;
	private static JTextField[] blank_fields = new JTextField[4];
	private static JLabel file_label = new JLabel("", SwingConstants.CENTER);

	public WelcomePage() {
		super();
		welcomepage = this;
		for (int i = 1; i < 5; ++i) {
			teamNames.add("Team " + Integer.toString(i));
		}
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.add(addFileChooser());
		center.add(addSlider());
		JCheckBox quickPlay = new JCheckBox("QuickPlay");
		quickPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quick = true;
			}
		});
		quickPlay.setForeground(Color.white);
		center.add(quickPlay);
		center.add(addTeams());
		center.setOpaque(true);
		center.setBackground(Color.BLUE);
		add(addWelcome(), BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(addBottom(), BorderLayout.SOUTH);
		setSize(1000, 600);
		setLocation(0, 0);
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
		setVisible(true);
	}

	public JButton getStart() {
		return start;
	}

	public ArrayList<String> getTeamNames() {
		return teamNames;
	}

	public int getNumTeams() {
		return numTeams;
	}

	public String getFileName() {
		return file;
	}

	public static JPanel addWelcome() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		JLabel WelcomeLabel = new JLabel("Welcome to Jeopardy!", SwingConstants.CENTER);
		WelcomeLabel.setFont(new Font("SanSerif", 1, 24));
		panel.add(WelcomeLabel);
		JLabel WelcomeExplain = new JLabel(
				"Chose the game file, number of teams, and team names before starting the game.",
				SwingConstants.CENTER);
		panel.setOpaque(true);
		panel.setBackground(Color.cyan);
		panel.add(WelcomeExplain);
		return panel;
	}

	@SuppressWarnings("static-access")
	private static JPanel addFileChooser() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JLabel file_text = new JLabel("Please choose a game file.", SwingConstants.CENTER);
		file_text.setForeground(Color.WHITE);
		JButton file_button = new JButton("Choose File");
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.Y_AXIS));
		button_panel.setBackground(Color.BLUE);
		button_panel.setOpaque(true);
		button_panel.add(Box.createVerticalGlue());
		button_panel.add(file_button);
		button_panel.add(Box.createVerticalGlue());
		file_button.setAlignmentX(panel.CENTER_ALIGNMENT);
		panel.add(file_text);
		panel.add(button_panel);
		file_button.setOpaque(true);
		file_button.setBackground(Color.gray);
		file_button.setForeground(Color.white);
		file_button.setBorderPainted(false);
		file_label.setForeground(Color.white);
		panel.add(file_label);
		file_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser file_choice = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt", "text");
				file_choice.setFileFilter(filter);
				int approve = file_choice.showOpenDialog(file_choice);
				if (approve == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = file_choice.getSelectedFile();
						file = selectedFile.getAbsolutePath();
						file_label.setText(selectedFile.getName());
						new GameData(file);
						startMaybe();
					} catch (Exception exception) {
						JOptionPane.showMessageDialog(new JFrame(),
								" Check file  " + file_label.getText() + " \n" + exception.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
						file = "";
						file_label.setText("");
					}
				}
			}
		});

		panel.setBackground(Color.BLUE);
		panel.setOpaque(true);
		return panel;
	}

	private static JPanel addSlider() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		panel.setBackground(Color.BLUE);
		JLabel file_text = new JLabel("Please choose the number of teams that will be playing on the slider below.",
				SwingConstants.CENTER);
		panel.add(file_text);
		JSlider slider = new JSlider(1, 4);
		panel.add(slider);
		file_text.setForeground(Color.WHITE);
		slider.setFont(new Font("Calibri", Font.BOLD, 10));
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.setAutoscrolls(true);
		slider.setSnapToTicks(true);
		slider.setForeground(Color.WHITE);
		slider.setBackground(Color.gray);
		slider.setOpaque(true);
		slider.setValue(0);
		panel.setOpaque(true);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				numTeams = slider.getValue();
				for (int i = 1; i < 4; i++) {
					panelList.get(i).setVisible(i < numTeams);
					teamNames.set(i, "Team " + i);
				}
				startMaybe();
			}
		});
		return panel;
	}

	@SuppressWarnings("static-access")
	private static JPanel addTeams() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		for (int i = 1; i < 5; ++i) {
			JPanel tempTeamPanel = new JPanel(), blankInputPanel = new JPanel();
			blank_fields[i - 1] = new JTextField(14);
			blankInputPanel.setBackground(Color.BLUE);
			blankInputPanel.setOpaque(true);
			blankInputPanel.add(blank_fields[i - 1]);
			blank_fields[i - 1].setBackground(Color.CYAN);
			tempTeamPanel.setLayout(new BoxLayout(tempTeamPanel, BoxLayout.Y_AXIS));
			JLabel text = new JLabel("\t\t\t\t\t\tPlease name Team " + i + "\t\t\t\t\t\t");
			text.setOpaque(true);
			text.setBackground(Color.gray);
			text.setForeground(Color.WHITE);
			text.setAlignmentX(tempTeamPanel.CENTER_ALIGNMENT);
			tempTeamPanel.add(text);
			tempTeamPanel.setBackground(Color.blue);
			tempTeamPanel.setOpaque(true);
			tempTeamPanel.setVisible(false);
			final int icopy = i - 1;
			blank_fields[icopy].addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					teamNames.set(icopy, blank_fields[icopy].getText());
					startMaybe();
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}
			});
			tempTeamPanel.add(blankInputPanel);
			panelList.add(tempTeamPanel);
			panel.add(tempTeamPanel);
		}
		panelList.get(0).setVisible(true);
		panel.setBackground(Color.blue);
		panel.setOpaque(true);
		return panel;
	}

	private static void startMaybe() {
		boolean startable = !file.equals("");
		for (int i = 0; i < numTeams; ++i) {
			if (blank_fields[i].getText().isEmpty()) {
				startable = false;
				break;
			}
		}
		start.setEnabled(startable);

	}

	private JPanel addBottom() {
		JPanel panel = new JPanel(), panel2 = new JPanel(), panel3 = new JPanel(), panel4 = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		start.setOpaque(true);
		start.setBackground(Color.gray);
		start.setForeground(Color.white);
		start.setBorderPainted(false);
		start.setEnabled(false);
		JButton clear = new JButton("Clear Choices");
		clear.setOpaque(true);
		clear.setBackground(Color.gray);
		clear.setForeground(Color.white);
		clear.setBorderPainted(false);

		JButton exit = new JButton("Exit");
		exit.setOpaque(true);
		exit.setBackground(Color.gray);
		exit.setForeground(Color.white);
		exit.setBorderPainted(false);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				welcomepage.dispose();
				panelList = new ArrayList<JPanel>(4);
				teamNames = new ArrayList<String>(4);
				welcomepage = new WelcomePage();
				numTeams = 1;
				file = "";
			}
		});
		panel4.add(clear);
		panel3.add(exit);
		panel2.add(start);
		panel.add(panel2);
		panel.add(panel4);
		panel.add(panel3);
		panel.setOpaque(true);
		panel.setBackground(Color.blue);
		panel2.setOpaque(true);
		panel2.setBackground(Color.blue);
		panel3.setOpaque(true);
		panel3.setBackground(Color.blue);
		panel4.setOpaque(true);
		panel4.setBackground(Color.blue);
		return panel;
	}

}
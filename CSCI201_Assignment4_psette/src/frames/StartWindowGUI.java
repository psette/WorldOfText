package frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import game_logic.GameData;
import game_logic.User;
import listeners.ExitWindowListener;
import listeners.TextFieldFocusListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;
import server.GameClient;
import server.GameServer;

public class StartWindowGUI extends JFrame {
	private static final long serialVersionUID = 8348372531060299461L;
	private JPanel cards;
	private JPanel portPanel = new JPanel();
	private JRadioButton notNetworked = new JRadioButton("Not Networked"), host = new JRadioButton("Host"),
			join = new JRadioButton("Join");
	private JPanel mainPanel;
	private JFileChooser fileChooser;
	private JButton fileChooserButton;
	private JTextField portField, ip;
	private List<JTextField> teamNameTextFields;
	private List<JLabel> teamNameLabels;
	private static final int MAX_NUMBER_OF_TEAMS = 4;
	private int numberOfTeams;
	private JButton startGameButton;
	private JButton clearDataButton;
	private JLabel waitingLabel;
	private JButton exitButton;
	private JSlider slider;
	private GameServer gs;
	private JLabel fileNameLabel;
	private JButton logoutButton;
	private JCheckBox quickPlay;
	private Boolean haveNames;
	private Boolean haveValidFile;
	private GameData gameData;
	private StartWindowGUI startWindowGUI;
	private JLabel welcomeLabel = new JLabel("Welcome to Jeopardy!"),
			explainContentLabel = new JLabel(
					"Choose whether you are joining or hosting a game or playing non-networked."),
			numberOfTeamsLabel = new JLabel(
					"Please choose the number of teams that will be playing on the slider below.");
	private JPanel radioButtonsPanel = new JPanel();
	private JLabel chooseGameFileLabel = new JLabel("Please choose a game file.");
	// logged in user
	private User loggedInUser;

	public StartWindowGUI(User user) {

		super("Jeopardy Menu");
		loggedInUser = user;
		numberOfTeams = -1;
		haveNames = false;
		haveValidFile = false;
		cards = new JPanel();
		startWindowGUI = this;
		cards.setLayout(new CardLayout());
		initializeComponents();
		createGUI();
		addListeners();
	}

	// private methods
	private void initializeComponents() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		portField = new JTextField("port");
		ip = new JTextField("IP Address");
		portField.setVisible(false);
		ip.setVisible(false);
		AppearanceSettings.setSize(150, 50, portField, ip);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, ip, portField);
		ip.addFocusListener(new TextFieldFocusListener("IP Address", ip));
		portField.addFocusListener(new TextFieldFocusListener("port", portField));
		fileChooser = new JFileChooser();
		teamNameTextFields = new ArrayList<>(4);
		teamNameLabels = new ArrayList<>(4);
		fileNameLabel = new JLabel("");
		logoutButton = new JButton("Logout");
		// fileRating = new JLabel("");
		quickPlay = new JCheckBox("Quick Play?");

		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {
			teamNameTextFields.add(new JTextField());
			teamNameLabels.add(new JLabel("Please name Team " + (i + 1)));
		}

		startGameButton = new JButton("Start Jeopardy");
		clearDataButton = new JButton("Clear Choices");
		exitButton = new JButton("Exit");
		fileChooserButton = new JButton("Choose File");
		slider = new JSlider();

	}

	private void createGUI() {
		// setting appearance of member variable gui components
		// setting background colors
		AppearanceSettings.setBackground(Color.darkGray, exitButton, logoutButton, clearDataButton, startGameButton,
				slider, teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton);

		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, teamNameTextFields.get(0),
				teamNameTextFields.get(1), teamNameTextFields.get(2), teamNameTextFields.get(3));

		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, fileNameLabel, mainPanel);

		// setting fonts
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, teamNameTextFields.get(0), teamNameTextFields.get(1),
				teamNameTextFields.get(2), teamNameTextFields.get(3), teamNameLabels.get(0), teamNameLabels.get(1),
				teamNameLabels.get(2), teamNameLabels.get(3), fileChooserButton, fileNameLabel, exitButton,
				clearDataButton, logoutButton, startGameButton);

		// other
		AppearanceSettings.setForeground(Color.lightGray, exitButton, logoutButton, clearDataButton, startGameButton,
				teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton, fileNameLabel, slider);

		AppearanceSettings.setOpaque(exitButton, clearDataButton, logoutButton, startGameButton, slider,
				teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton);

		AppearanceSettings.setSize(180, 70, exitButton, clearDataButton, startGameButton, logoutButton);
		AppearanceSettings.setSize(150, 80, teamNameTextFields.get(0), teamNameTextFields.get(1),
				teamNameTextFields.get(2), teamNameTextFields.get(3));

		AppearanceSettings.setSize(250, 100, teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2),
				teamNameLabels.get(3));

		AppearanceSettings.unSetBorderOnButtons(exitButton, logoutButton, clearDataButton, startGameButton,
				fileChooserButton);

		AppearanceSettings.setTextAlignment(teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2),
				teamNameLabels.get(3), fileNameLabel);

		setAllInvisible(teamNameTextFields, teamNameLabels);
		// check box settings
		quickPlay.setFont(AppearanceConstants.fontSmallest);
		quickPlay.setHorizontalTextPosition(SwingConstants.LEFT);
		quickPlay.setPreferredSize(new Dimension(200, 30));

		// file chooser settings
		fileChooser.setPreferredSize(new Dimension(400, 500));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));

		// slider settings
		AppearanceSettings.setSliders(1, MAX_NUMBER_OF_TEAMS, 1, 1, AppearanceConstants.fontSmallest, slider);
		slider.setSnapToTicks(true);
		slider.setPreferredSize(new Dimension(500, 50));
		startGameButton.setEnabled(false);

		createMainPanel();

		setBackground(AppearanceConstants.darkBlue);
		add(mainPanel, BorderLayout.CENTER);
		setSize(800, 825);
	}

	// sets the label and textField visible again
	private void setVisible(JLabel label, JTextField textField) {
		// the first text field is always shown so we can use their border
		Border border = teamNameTextFields.get(0).getBorder();

		textField.setBackground(AppearanceConstants.lightBlue);
		textField.setForeground(Color.black);
		textField.setBorder(border);
		label.setBackground(Color.darkGray);
		label.setForeground(Color.lightGray);
	}

	// I wanted to user BoxLayout for resizability but if you simply set a
	// components invisble with
	// setVisible(false), it changes the size of the components that are
	// visible. This is my way aroung that
	private void setInvisible(JLabel label, JTextField textField) {
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, textField, label);
		AppearanceSettings.setForeground(AppearanceConstants.darkBlue, textField, label);
		textField.setBorder(AppearanceConstants.blueLineBorder);
	}

	// used in the constructor to set everything invisible (except the first
	// label and text field)
	private void setAllInvisible(List<JTextField> teamNameTextFields, List<JLabel> teamNameLabels) {

		for (int i = 1; i < 4; i++)
			setInvisible(teamNameLabels.get(i), teamNameTextFields.get(i));
	}

	@SuppressWarnings("static-access")
	private void createMainPanel() {
		// initialize local panels
		JPanel waitingPanel = new JPanel();
		JPanel teamNamesPanel = new JPanel();
		JPanel teamLabelsPanel1 = new JPanel();
		JPanel teamLabelsPanel2 = new JPanel();
		JPanel teamTextFieldsPanel1 = new JPanel();
		JPanel teamTextFieldsPanel2 = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel teamsAndFilePanel = new JPanel();
		JPanel numberOfTeamsPanel = new JPanel();
		JPanel fileChooserPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel welcomePanel = new JPanel(new BorderLayout());
		JPanel titlePanel = new JPanel(new BorderLayout());
		radioButtonsPanel.setLayout(new GridLayout(1, 3));
		waitingLabel = new JLabel("");
		// set appearances on local variables
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, welcomeLabel, explainContentLabel, welcomePanel,
				radioButtonsPanel, titlePanel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, explainContentLabel, chooseGameFileLabel,
				notNetworked, join, host, numberOfTeamsLabel, waitingLabel);
		AppearanceSettings.setTextAlignment(welcomeLabel, explainContentLabel, chooseGameFileLabel, numberOfTeamsLabel,
				waitingLabel);

		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, chooseGameFileLabel, numberOfTeamsLabel,
				numberOfTeamsPanel, fileChooserPanel, teamsAndFilePanel, waitingPanel, buttonPanel, teamNamesPanel,
				teamLabelsPanel1, teamLabelsPanel2, teamTextFieldsPanel1, teamTextFieldsPanel2);
		AppearanceSettings.setForeground(Color.lightGray, chooseGameFileLabel, numberOfTeamsLabel, waitingLabel);
		ButtonGroup group = new ButtonGroup();
		group.add(notNetworked);
		group.add(host);
		group.add(join);
		notNetworked.setSelected(true);
		notNetworked.setHorizontalAlignment(notNetworked.CENTER);
		host.setHorizontalAlignment(host.CENTER);
		join.setHorizontalAlignment(join.CENTER);
		radioButtonsPanel.add(notNetworked);
		radioButtonsPanel.add(host);
		radioButtonsPanel.add(join);
		AppearanceSettings.setSize(800, 60, welcomePanel, explainContentLabel);
		AppearanceSettings.setSize(800, 100, buttonPanel, numberOfTeamsPanel);
		AppearanceSettings.setSize(800, 80, fileChooserPanel);

		welcomeLabel.setFont(AppearanceConstants.fontLarge);
		numberOfTeamsLabel.setSize(700, 60);

		// setting box layouts of panels
		AppearanceSettings.setBoxLayout(BoxLayout.LINE_AXIS, buttonPanel, fileChooserPanel, teamLabelsPanel1,
				teamLabelsPanel2, teamTextFieldsPanel1, teamTextFieldsPanel2);
		AppearanceSettings.setBoxLayout(BoxLayout.PAGE_AXIS, northPanel, teamNamesPanel, teamsAndFilePanel,
				numberOfTeamsPanel);

		// method iterates through components and add glue after each one is
		// added, bool indicates whether glue should be added at the initially
		// as well
		AppearanceSettings.addGlue(teamLabelsPanel1, BoxLayout.LINE_AXIS, true, teamNameLabels.get(0),
				teamNameLabels.get(1));
		AppearanceSettings.addGlue(teamLabelsPanel2, BoxLayout.LINE_AXIS, true, teamNameLabels.get(2),
				teamNameLabels.get(3));
		AppearanceSettings.addGlue(teamTextFieldsPanel1, BoxLayout.LINE_AXIS, true, teamNameTextFields.get(0),
				teamNameTextFields.get(1));
		AppearanceSettings.addGlue(teamTextFieldsPanel2, BoxLayout.LINE_AXIS, true, teamNameTextFields.get(2),
				teamNameTextFields.get(3));
		AppearanceSettings.addGlue(teamNamesPanel, BoxLayout.PAGE_AXIS, true, teamLabelsPanel1, teamTextFieldsPanel1,
				teamLabelsPanel2, teamTextFieldsPanel2);

		// don't want to pass in fileNameLabel since I don't want glue after it
		AppearanceSettings.addGlue(fileChooserPanel, BoxLayout.LINE_AXIS, true, chooseGameFileLabel, fileChooserButton);
		fileChooserPanel.add(fileNameLabel);

		// don't want to pass in fileChooserPanel since I don't want glue after
		// it
		portPanel.setBackground(AppearanceConstants.darkBlue);
		portPanel.add(portField);
		portPanel.add(ip);
		teamsAndFilePanel.add(Box.createGlue());
		teamsAndFilePanel.add(portPanel);
		teamsAndFilePanel.add(numberOfTeamsPanel);
		teamsAndFilePanel.add(fileChooserPanel);

		AppearanceSettings.addGlue(buttonPanel, BoxLayout.LINE_AXIS, true, startGameButton, clearDataButton,
				logoutButton, exitButton);

		// add other components to other containers
		welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
		welcomePanel.add(quickPlay, BorderLayout.EAST);

		titlePanel.add(welcomePanel, BorderLayout.NORTH);
		titlePanel.add(explainContentLabel, BorderLayout.CENTER);
		titlePanel.add(radioButtonsPanel, BorderLayout.SOUTH);
		northPanel.add(titlePanel);

		numberOfTeamsPanel.add(numberOfTeamsLabel);
		numberOfTeamsPanel.add(slider);
		waitingPanel.add(waitingLabel);
		mainPanel.add(northPanel);
		JPanel panel = new JPanel();
		cards.add(panel, "normal");
		mainPanel.add(cards);
		panel.setLayout(new GridLayout(2, 1));
		panel.add(teamsAndFilePanel);
		panel.add(teamNamesPanel);
		mainPanel.add(waitingPanel);
		mainPanel.add(buttonPanel);
	}

	// determines whether the chosen file is valid
	private void setHaveValidFile(File file) {

		// if they had already chosen a valid file, but want to replace it, need
		// to clear stored data
		if (haveValidFile)
			gameData.clearData();

		try {
			// try parsing this file; the parseFile method could throw an
			// exception here, in which case we know it was invalid
			gameData.parseFile(file.getAbsolutePath());
			haveValidFile = true;

			if (gameData.getGameFile().getNumberOfRatings() == -1)
				fileNameLabel.setText(file.getName() + "    no rating");

			else
				fileNameLabel.setText(
						file.getName() + "    average rating: " + gameData.getGameFile().getAverageRating() + "/5");
			// check if the user can start the game
			startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());
		}

		catch (Exception e) {
			haveValidFile = false;
			startGameButton.setEnabled(false);
			fileNameLabel.setText("");
			// pop up with error message
			JOptionPane.showMessageDialog(this, e.getMessage(), "File Reading Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean haveValidPort() {
		String portString, ipString;
		portString = portField.getText().trim();
		ipString = ip.getText().trim();
		if (notNetworked.isSelected()) {
			return true;
		} else {
			if (portString.equals("") || portString.equals("port")) {
				return false;
			} else if (host.isSelected()) {
				return true;
			} else if (ipString.equals("") || ipString.equals("IP Address")) {
				return false;
			}
		}
		return true;
	}

	private void addListeners() {

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this));
		portField.getDocument().addDocumentListener(new MyDocumentListener());
		ip.getDocument().addDocumentListener(new MyDocumentListener());
		// adding a document listener to each text field. This will allow us to
		// determine if the user has entered team names
		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {
			teamNameTextFields.get(i).getDocument().addDocumentListener(new MyDocumentListener());
		}
		gameData = new GameData();

		fileChooserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(StartWindowGUI.this);
				File file = fileChooser.getSelectedFile();

				if (file != null)
					setHaveValidFile(file);
			}

		});

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				// sets appropriate text fields and labels invisible
				for (int i = slider.getValue(); i < MAX_NUMBER_OF_TEAMS; i++) {
					setInvisible(teamNameLabels.get(i), teamNameTextFields.get(i));
				}
				// sets appropriate text fields and labels visible
				for (int i = 0; i < slider.getValue(); i++) {
					setVisible(teamNameLabels.get(i), teamNameTextFields.get(i));
				}
				if (host.isSelected()) {
					for (int i = 1; i < MAX_NUMBER_OF_TEAMS; i++) {
						setInvisible(teamNameLabels.get(i), teamNameTextFields.get(i));
					}
				}
				// check if the user can start the game
				startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());
			}

		});
		host.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				slider.setVisible(true);
				portPanel.setVisible(true);
				portField.setVisible(true);
				numberOfTeamsLabel.setVisible(true);
				chooseGameFileLabel.setVisible(true);
				ip.setVisible(false);
				slider.setValue(2);
				slider.setMinimum(2);
				fileChooserButton.setVisible(true);
				startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());
				repaint();
			}
		});
		notNetworked.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				numberOfTeamsLabel.setVisible(true);
				chooseGameFileLabel.setVisible(true);
				slider.setVisible(true);
				portPanel.setVisible(false);
				teamNameLabels.get(0).setText("Please name Team 1");
				slider.setMinimum(1);
				slider.setValue(1);
				fileChooserButton.setVisible(true);
				startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());
			}
		});
		join.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quickPlay.setVisible(false);
				numberOfTeamsLabel.setVisible(false);
				chooseGameFileLabel.setVisible(false);
				slider.setValue(0);
				teamNameLabels.get(0).setText("Please choose a team name");
				fileChooserButton.setVisible(false);
				portPanel.setVisible(true);
				portField.setVisible(true);
				slider.setVisible(false);
				fileNameLabel.setVisible(false);
				ip.setVisible(true);
				startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());

			}
		});

		startGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (join.isSelected()) {

					// if (gs.addTeam(teamNameTextFields.get(0)
					// .getText()) == false /*
					// * team name already used
					// */) {
					// teamNameTextFields.get(0).setText("");
					// JOptionPane.showMessageDialog(null,
					// "Team name " + teamNameTextFields.get(0).getText() +
					// "already used", "Error",
					// JOptionPane.ERROR_MESSAGE);
					//
					// }
					new GameClient(ip.getText(), Integer.parseInt(portField.getText()),
							teamNameTextFields.get(0).getText(), startWindowGUI);
					setDisabled();
				} else {
					gameData.setNumberOfQuestions(quickPlay.isSelected() ? 5 : 25);
					numberOfTeams = slider.getValue();
					if (host.isSelected()) {
						setGs(new GameServer(Integer.valueOf(portField.getText()), numberOfTeams, startWindowGUI,
								(teamNameTextFields.get(0).getText().trim())));
						setDisabled();
						gs.addData(gameData);
						System.out.println("Going to create first client");
						GameClient client = new GameClient("localhost", Integer.valueOf(portField.getText()),
								teamNameTextFields.get(0).getText(), startWindowGUI);
						client.setHost();

					} else {
						List<String> teamNames = new ArrayList<>(numberOfTeams);
						// getting the text in each of the visible text fields
						// and
						// storing them in a list
						for (int i = 0; i < numberOfTeams; i++) {
							teamNames.add(teamNameTextFields.get(i).getText());
						}
						// initializing TeamGUIComponents objects
						gameData.networked = false;
						gameData.setTeams(teamNames, numberOfTeams);

						new MainGUI(gameData, loggedInUser, false, null, true).setVisible(true);
						dispose();
					}
				}
			}
		});

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}

		});

		clearDataButton.addActionListener(new ActionListener() {

			// reseting all data
			@Override
			public void actionPerformed(ActionEvent e) {
				haveNames = false;
				haveValidFile = false;
				gameData.clearData();
				quickPlay.setSelected(false);
				// start index at 1, we still was to show the 0th elements (team
				// 1)
				for (int i = 1; i < MAX_NUMBER_OF_TEAMS; i++) {
					setInvisible(teamNameLabels.get(i), teamNameTextFields.get(i));
					teamNameTextFields.get(i).setText("");
				}

				startGameButton.setEnabled(false);
				teamNameTextFields.get(0).setText("");
				slider.setValue(1);
				fileNameLabel.setText("");
			}

		});

		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginGUI().setVisible(true);
				dispose();
			}

		});

	}

	// updates and returns the haveNames member variable of whether all teams
	// have been named
	private boolean haveNamesAndFile() {
		if (join.isSelected()) {
			return !teamNameTextFields.get(0).getText().trim().equals("");
		}
		if (host.isSelected()) {
			return !teamNameTextFields.get(0).getText().trim().equals("") && haveValidFile;
		}
		haveNames = true;
		// check to see if all relevant text fields have text in them
		for (int i = 0; i < slider.getValue(); i++) {

			if (teamNameTextFields.get(i).getText().trim().equals(""))
				haveNames = false;
		}

		return haveNames && haveValidFile;
	}

	public GameServer getGs() {
		return gs;
	}

	public void setGs(GameServer gs) {
		this.gs = gs;
	}

	// document listener; in each method, simply checking whether the user can
	// start the game
	private class MyDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			startGameButton.setEnabled(haveNamesAndFile() && haveValidPort());
		}
	}

	public void setWaiting(String string) {
		waitingLabel.setVisible(true);
		waitingLabel.setText(string);
		repaint();
	}

	public StartWindowGUI getStartWindowGUI() {
		return startWindowGUI;
	}

	public void setStartWindowGUI(StartWindowGUI startWindowGUI) {
		this.startWindowGUI = startWindowGUI;
	}

	public void setDisabled() {
		quickPlay.setEnabled(false);
		join.setEnabled(false);
		notNetworked.setEnabled(false);
		host.setEnabled(false);
		ip.setEditable(false);
		portField.setEditable(false);
		slider.setEnabled(false);
		fileChooserButton.setEnabled(false);
		teamNameTextFields.get(0).setEditable(false);
		startGameButton.setEnabled(false);
		clearDataButton.setEnabled(false);
	}
}

package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import game_logic.GameData;
import game_logic.TeamData;
import listeners.ExitWindowListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class WinnersGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel andTheWinnersAre;
	private JTextPane winners;
	private GameData gameData;
	private JButton okay;
	private JPanel centerPanel;
	private String averageRating;
	private JSlider slider;

	public WinnersGUI(GameData gameData) {
		this.gameData = gameData;
		averageRating = gameData.getRating();
		slider = new JSlider();
		initializeComponents();
		createGUI();
		addListeners();
	}

	// private methods
	private void initializeComponents() {
		andTheWinnersAre = new JLabel("");
		winners = new JTextPane();
		okay = new JButton("Okay");
		okay.setEnabled(false);
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel northPanel = new JPanel();
		centerPanel = new JPanel();

		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, centerPanel, winners, andTheWinnersAre,
				mainPanel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, okay, andTheWinnersAre);

		winners.setEditable(false);
		// centers the text
		// sourced from:
		// http://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
		StyledDocument doc = winners.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		winners.setFont(AppearanceConstants.fontLarge);
		okay.setBackground(Color.gray);
		okay.setForeground(AppearanceConstants.darkBlue);

		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));

		List<TeamData> winnersList = gameData.getFinalistsAndEliminatedTeams().getWinners();
		// no winners
		if (winnersList.size() == 0) {
			andTheWinnersAre.setText("Sad!");
			winners.setText("There were no winners!");
		}
		// at least 1 winner
		else {
			String winnersAre = winnersList.size() == 1 ? "And the winner is..." : "And the winners are...";
			StringBuilder teamsBuilder = new StringBuilder();
			teamsBuilder.append(winnersList.get(0).getTeamName());

			for (int i = 1; i < winnersList.size(); i++) {
				teamsBuilder.append(" and " + winnersList.get(i).getTeamName());
			}

			andTheWinnersAre.setText(winnersAre);
			winners.setText(teamsBuilder.toString());
		}
		centerPanel.add(addCenter());
		mainPanel.add(andTheWinnersAre, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(okay, BorderLayout.SOUTH);

		setSize(300, 300);
		add(mainPanel, BorderLayout.CENTER);
	}

	private JPanel addCenter() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		JLabel instruct = new JLabel("Please rate this game on a scale of 1 to 5"),
				rating = new JLabel("Average rating is " + averageRating);
		instruct.setHorizontalAlignment(instruct.CENTER);
		rating.setHorizontalAlignment(rating.CENTER);
		AppearanceSettings.setSliders(1, 5, 1, 1, AppearanceConstants.fontSmallest, slider);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, main, instruct, slider, rating);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, instruct, rating);
		slider.setSnapToTicks(true);
		slider.setValue(1);
		main.add(winners);
		main.add(instruct);
		main.add(rating);
		main.add(slider);
		return main;
	}

	private void addListeners() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this));
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				okay.setEnabled(true);

			}
		});
		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				slider.getValue();
				String[] temp = averageRating.split("/");
				temp[0] = String
						.valueOf((int) (Double.parseDouble(temp[0]) * gameData.getPeople() + slider.getValue()));
				gameData.writeFile(temp[0]);
				dispose();
			}

		});
	}
}

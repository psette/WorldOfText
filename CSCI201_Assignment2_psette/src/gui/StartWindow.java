package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import game_logic.TeamData;

public class StartWindow {
	private static StartWindow startWindow;
	private static WelcomePage welcomepage;
	private MainFrame mf;

	public StartWindow() {
		startWindow = this;
		welcomepage = new WelcomePage();
		welcomepage.getStart().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = welcomepage.getFileName();
				int numTeams = welcomepage.getNumTeams();
				ArrayList<String> TeamNames = welcomepage.getTeamNames();
				// determines whether the user gave a valid input
				boolean successful = true;

				ArrayList<TeamData> teamData = new ArrayList<>(numTeams);
				Set<String> duplicateTeamNamesCheck = new HashSet<>();

				// choose team names
				String teamname = "";
				for (int i = 1; i <= numTeams; i++) {
					teamname = TeamNames.get(i - 1);
					if (duplicateTeamNamesCheck.contains(teamname.toLowerCase())) {
						successful = false;
						break;
					} else {
						teamData.add(new TeamData(i - 1, 0L, teamname));
						duplicateTeamNamesCheck.add(teamname.toLowerCase());
					}
				}
				if (welcomepage.getFileName() == "") {
					JOptionPane.showMessageDialog(new JFrame(), "Please choose a file.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!successful) {
					JOptionPane.showMessageDialog(new JFrame(), " Error with team name: " + teamname, "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					boolean quickPlay = welcomepage.quick;
					welcomepage.dispose();
					mf = new MainFrame(file, teamData, quickPlay);
					kill();
				}
			}
		});
	}

	public void kill() {
		mf.newFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (welcomepage != null) {
					welcomepage.dispose();
				}
				welcomepage = null;
				mf.dispose();
				startWindow = null;
				startWindow = new StartWindow();
			}
		});
	}

}

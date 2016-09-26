package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import game_logic.QuestionAnswer;

public class QuestionPage extends JPanel {
	public static final long serialVersionUID = 1;
	public JLabel errorMsg = new JLabel("Invalid question format. Remember to pose your answer as a question.");
	public JButton submitButton = new JButton("Submit Answer");
	private static QuestionAnswer question;
	public JTextField blank_field = new JTextField(14);

	public QuestionPage(String team, String category, int pointValue, QuestionAnswer questionIn) {
		super();
		question = questionIn;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(addTop(team, category, pointValue));
		add(addQuestion());
	}

	private JPanel addQuestion() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		main.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		JTextArea area = new JTextArea(20, 35);
		area.setEditable(false);
		area.setText(question.getQuestion());
		JPanel blankInputPanel = new JPanel();
		blankInputPanel.setBackground(Color.GRAY);
		blankInputPanel.setOpaque(true);
		blank_field.setBackground(Color.WHITE);
		blankInputPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 10;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.weightx = 1;
		c.gridy = 0;
		blankInputPanel.add(blank_field, c);
		blankInputPanel.add(submitButton);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 10;
		c.gridy = 0;
		JScrollPane scrollPane = new JScrollPane(area);
		errorMsg.setVisible(false);
		main.add(errorMsg);
		main.add(scrollPane);
		main.add(blankInputPanel);
		main.setBackground(Color.gray);
		return main;
	}

	private JPanel addTop(String team, String category, int pointValue) {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
		JLabel teamName = new JLabel(team);
		teamName.setOpaque(true);
		teamName.setFont(new Font("SanSerif", 1, 24));
		teamName.setBackground(Color.BLUE);
		teamName.setForeground(Color.WHITE);
		JPanel temp = new JPanel();
		temp.setBackground(Color.BLUE);
		temp.add(teamName);
		main.add(temp);
		JLabel categoryText = new JLabel(category);
		categoryText.setFont(new Font("SanSerif", 1, 24));
		categoryText.setOpaque(true);
		categoryText.setBackground(Color.BLUE);
		categoryText.setForeground(Color.WHITE);
		temp = new JPanel();
		temp.setBackground(Color.BLUE);
		temp.add(categoryText);
		main.add(temp);
		JLabel pointText = new JLabel("$" + Integer.toString(pointValue));
		pointText.setFont(new Font("SanSerif", 1, 24));
		pointText.setOpaque(true);
		pointText.setBackground(Color.BLUE);
		pointText.setForeground(Color.WHITE);
		temp = new JPanel();
		temp.setBackground(Color.BLUE);
		temp.add(pointText);
		main.add(temp);
		return main;
	}

}

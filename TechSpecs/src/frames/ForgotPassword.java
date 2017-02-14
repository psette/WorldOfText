package frames;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import listeners.TextFieldFocusListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class ForgotPassword extends JFrame {
	private static final long serialVersionUID = -8277839502614106546L;
	private JButton check, returnBut;
	private JPanel buttonPanel;
	private JTextField userField;
	private LoginGUI loginGUI;

	public ForgotPassword(LoginGUI loginGUI) {
		this.loginGUI = loginGUI;
		add(createPanel());
		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

	}

	public JButton getReturn() {
		return returnBut;
	}

	private void createButtons() {
		returnBut = new JButton("Return to login page ↩");
		returnBut.setOpaque(true);
		AppearanceSettings.unSetBorderOnButtons(returnBut);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, returnBut);
		returnBut.setHorizontalAlignment(SwingConstants.LEFT);
		buttonPanel = new JPanel();
		check = new JButton("Check Account");
		check.setForeground(Color.lightGray);
		check.setSize(200, 80);
		check.setBackground(Color.darkGray);
		check.setOpaque(true);
		AppearanceSettings.unSetBorderOnButtons(check);
		check.setFont(AppearanceConstants.fontSmall);
		buttonPanel.add(check);
		buttonPanel.setBackground(Color.pink);
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkAccount();
			}
		});
	}

	protected void tryLogin() {
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/worldoftext?user=root&password=root&useSSL=false");
			st = conn.createStatement();
			ps = conn.prepareStatement("SELECT * FROM logininfo WHERE Login=?");
			ps.setString(1, userField.getText());
			rs = ps.executeQuery();
			if (!rs.next()) {
				JOptionPane.showMessageDialog(this,
						"Username not in database. \nPlease choose a username that is in the database.");

			} else {
				String s = JOptionPane.showInputDialog(this, rs.getString("SecurityQuestion"),
						"Please enter your security answer.");
				if (rs.getString("SecurityAnswer").equals(s)) {
					correct();
					dispose();
					loginGUI.setVisible(true);
				} else {
					userField.setText("");
					JOptionPane.showMessageDialog(this, "Please enter a correct security question answer.");
				}
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException 2: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}

	private void correct() {
		String s = JOptionPane.showInputDialog(this, "Please enter a new password.");
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/worldoftext?user=root&password=root&useSSL=false");
			st = conn.createStatement();
			ps = conn.prepareStatement("UPDATE logininfo SET Pwd=? Where Login=?");
			ps.setString(1, s);
			ps.setString(2, userField.getText());
			if (ps.executeUpdate() == 1) {
				JOptionPane.showMessageDialog(this, "Sucsessfully updated.", "Sucsess.", JOptionPane.WARNING_MESSAGE);
				loginGUI.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "Error with updating info.", "Error.", JOptionPane.WARNING_MESSAGE);
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException 3: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}

	}

	protected void checkAccount() {
		if (userField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please add a username", "Username field empty.",
					JOptionPane.WARNING_MESSAGE);
		} else {
			tryLogin();
		}
	}

	private JPanel createPanel() {
		createButtons();

		userField = new JTextField("Please enter your username.");
		AppearanceSettings.setSize(300, 60, userField);
		userField.addFocusListener(new TextFieldFocusListener("Please enter your username.", userField));

		JLabel label = new JLabel("Forgot Password");
		label.setFont(AppearanceConstants.fontLarge);

		JPanel main = new JPanel(), userPanel = new JPanel(), labelPanel = new JPanel();
		userPanel.add(userField);
		labelPanel.add(label);

		main.setLayout(new GridLayout(4, 1));
		main.add(returnBut);
		main.add(labelPanel);
		main.add(userPanel);
		main.add(buttonPanel);

		AppearanceSettings.setForeground(Color.lightGray, userField);
		AppearanceSettings.setTextAlignment(label);
		AppearanceSettings.setBackground(Color.pink, returnBut, label, labelPanel, userPanel);

		return main;
	}
}
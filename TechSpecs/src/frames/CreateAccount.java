package frames;

import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import listeners.TextFieldFocusListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class CreateAccount extends JFrame {
	private static final long serialVersionUID = -4336450123713302910L;
	private JTextField securityQ, securityA;
	private JButton create, returnBut;
	private JPanel buttonPanel, securityPanelQ, securityPanelA;
	private String username, password;
	private JLabel alertLabel;
	private LoginGUI loginGUI;

	public CreateAccount(String usernameString, String passwordString, LoginGUI loginGUI) {
		this.loginGUI = loginGUI;
		add(createPanel(usernameString, passwordString));
		setUsername(usernameString);
		setPassword(passwordString);
		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public JButton getReturn() {
		return returnBut;
	}

	private JPanel createPanel(String usernameString, String passwordString) {
		JPanel main = new JPanel();
		main.setLayout(new GridLayout(9, 1));
		createButtons();
		initializeSecurity();
		JLabel label = new JLabel("Create Account");
		label.setFont(AppearanceConstants.fontLarge);
		alertLabel = new JLabel("");
		JLabel user = new JLabel("Username: " + usernameString, SwingConstants.CENTER);
		user.setLayout(new BorderLayout());
		JLabel pass = new JLabel("Password: " + passwordString, SwingConstants.CENTER);
		AppearanceSettings.setTextAlignment(user, pass, alertLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, user, pass, alertLabel);
		main.add(returnBut);
		main.add(label);
		main.add(alertLabel);
		main.add(user);
		main.add(pass);
		main.add(securityPanelQ);
		main.add(securityPanelA);
		JPanel blank = new JPanel();
		main.add(blank);
		main.add(buttonPanel);
		AppearanceSettings.setTextAlignment(user, pass, label, alertLabel);
		AppearanceSettings.setBackground(Color.pink, returnBut, alertLabel, blank, user, pass, main, securityPanelQ,
				securityPanelA);
		return main;
	}

	private void initializeSecurity() {
		securityPanelQ = new JPanel();
		securityPanelA = new JPanel();
		securityQ = new JTextField("Please enter your security question.");
		securityA = new JTextField("Please enter your security answer.");
		AppearanceSettings.setSize(300, 60, securityQ, securityA);
		securityQ.addFocusListener(new TextFieldFocusListener("Please enter your security question.", securityQ));
		securityA.addFocusListener(new TextFieldFocusListener("Please enter your security answer.", securityA));
		AppearanceSettings.setForeground(Color.lightGray, securityQ, securityA);
		securityPanelQ.add(securityQ);
		securityPanelA.add(securityA);
	}

	private void createButtons() {
		buttonPanel = new JPanel();
		returnBut = new JButton("Return to login page ↩");
		returnBut.setOpaque(true);
		AppearanceSettings.unSetBorderOnButtons(returnBut);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, returnBut);
		returnBut.setHorizontalAlignment(SwingConstants.LEFT);
		create = new JButton("Create Account");
		create.setForeground(Color.lightGray);
		create.setSize(200, 80);
		create.setBackground(Color.darkGray);
		create.setOpaque(true);
		AppearanceSettings.unSetBorderOnButtons(create);
		create.setFont(AppearanceConstants.fontSmall);
		buttonPanel.add(create);
		buttonPanel.setBackground(Color.pink);
		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createAccount();

			}
		});
	}

	@SuppressWarnings("resource")
	public void createAccount() {
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
			ps.setString(1, username); // set first variable in prepared
										// statement
			rs = ps.executeQuery();
			if (rs.next()) {
				alertLabel.setText("This username has already been chosen by another user");
			} else {
				ps = conn.prepareStatement(
						"INSERT INTO logininfo(Login,Pwd,SecurityQuestion,SecurityAnswer,CurrentlyLoggedIn) VALUES(?,?,?,?,?)");
				ps.setString(1, username);
				ps.setString(2, password);
				ps.setString(3, securityQ.getText());
				ps.setString(4, securityA.getText());
				ps.setBoolean(5, true);
				ps.executeUpdate();
				dispose();
				alertLabel.setText("");
				loginGUI.setVisible(true);
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException 1: " + sqle.getMessage());
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

	@SuppressWarnings("unused")
	private boolean canPressButtons() {
		return (!securityQ.getText().isEmpty() && !securityQ.getText().equals("Please enter your security question.")
				&& !securityA.getText().equals("Please enter your security answer.") && !securityA.getText().isEmpty());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
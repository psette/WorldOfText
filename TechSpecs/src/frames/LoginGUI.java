package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import listeners.TextFieldFocusListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;
import game_logic.User;

public class LoginGUI extends JFrame {
	private JCheckBox visblePasswordCheck;
	private static final long serialVersionUID = -6957949909513502626L;
	private JButton loginButton;
	private JButton createAccount;
	private JButton forgotPassword, GuestMode;
	private JTextField username;
	private JPasswordField password;
	public JLabel alertLabel;
	private LoginGUI loginGUI;

	public LoginGUI() {
		initializeComponents();
		createGUI();
		addListeners();
		loginGUI = this;
		setLocationRelativeTo(null);
	}

	private void initializeComponents() {
		visblePasswordCheck = new JCheckBox("View password");
		GuestMode = new JButton("Play as Guest");
		loginButton = new JButton("Login");
		forgotPassword = new JButton("Forgot Account?");
		createAccount = new JButton("Create Account");
		username = new JTextField("username");
		password = new JPasswordField("password");
		password.setEchoChar('*');
		alertLabel = new JLabel();
	}

	protected void tryLogin(String usernameString, String passwordString) {

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
			ps.setString(1, usernameString); // set first variable in prepared
												// statement
			rs = ps.executeQuery();
			if (!rs.next()) {
				alertLabel.setText("This username does not exist.");
			} else if (!rs.getString("Pwd").equals(passwordString)) {
				alertLabel.setText("The password you provided does not match our records");
			} else {
				User user = new User(usernameString, passwordString);
				new MainGUI(user, true).setVisible(true);
				dispose();
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException 5: " + sqle.getMessage());
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

	private void createGUI() {

		this.setTitle("Login");
		setLocationRelativeTo(null);
		JPanel mainPanel = new JPanel();
		JPanel GuestLogin = new JPanel();
		JPanel textFieldOnePanel = new JPanel();
		JPanel textFieldTwoPanel = new JPanel();
		JLabel welcome = new JLabel("Play as guest or login for premium access", SwingConstants.CENTER);
		JLabel titleLabel = new JLabel("World of Text!", SwingConstants.CENTER);
		JPanel alertPanel = new JPanel();
		JPanel textFieldsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel forgotPanel = new JPanel();
		JPanel welcomePanel = new JPanel(new GridLayout(3, 1));
		forgotPanel.add(forgotPassword);
		forgotPanel.add(visblePasswordCheck);
		GuestMode.setSize(100, 40);
		forgotPassword.setSize(100, 40);
		// set mass component appearances
		AppearanceSettings.setForeground(Color.black, createAccount, GuestLogin, GuestMode, loginButton, password,
				username);
		AppearanceSettings.setSize(500, 60, password, username);

		AppearanceSettings.setSize(200, 80, loginButton, createAccount);

		AppearanceSettings.setOpaque(loginButton, GuestMode, forgotPassword, createAccount);
		// AppearanceSettings.unSetBorderOnButtons(loginButton, GuestMode,
		// forgotPassword, createAccount);

		AppearanceSettings.setTextAlignment(welcome, alertLabel, titleLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, password, alertLabel, username, GuestMode,
				forgotPassword, loginButton, createAccount, visblePasswordCheck);

		AppearanceSettings.setBackground(Color.pink, forgotPassword, mainPanel, welcome, alertLabel, titleLabel,
				alertPanel, forgotPanel, textFieldsPanel, buttonsPanel, welcomePanel, textFieldOnePanel,
				textFieldTwoPanel, GuestLogin, visblePasswordCheck);

		// other appearance settings
		welcome.setFont(AppearanceConstants.fontMedium);
		titleLabel.setFont(AppearanceConstants.fontXL);
		loginButton.setEnabled(false);
		AppearanceSettings.setBackground(Color.pink, loginButton, createAccount, GuestMode);
		createAccount.setEnabled(false);

		// add components to containers
		welcomePanel.add(welcome);
		welcomePanel.add(titleLabel);
		JLabel imageWrap = new JLabel(new ImageIcon("rsz_usc_seal.png"));
		welcomePanel.add(imageWrap);
		GuestLogin.add(GuestMode);
		alertPanel.add(alertLabel);
		textFieldOnePanel.add(username);
		textFieldTwoPanel.add(password);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// adds components to the panel with glue in between each
		AppearanceSettings.addGlue(buttonsPanel, BoxLayout.LINE_AXIS, true, loginButton, createAccount, GuestMode);

		AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, false, welcomePanel);
		// don't want glue in between the following two panels, so they are not
		// passed in to addGlue
		mainPanel.add(alertPanel);
		mainPanel.add(GuestLogin);
		mainPanel.add(textFieldOnePanel);
		mainPanel.add(textFieldTwoPanel);
		// mainPnale.add(forgotPasswordButton;)
		mainPanel.add(forgotPanel);
		mainPanel.add(buttonsPanel);

		add(mainPanel, BorderLayout.CENTER);
		setSize(1000, 1000);
	}

	// returns whether the buttons should be enabled
	private boolean canPressButtons() {
		return (!username.getText().isEmpty() && !username.getText().equals("username")
				&& !String.valueOf(password.getPassword()).equals("password")
				&& !String.valueOf(password.getPassword()).isEmpty());
	}

	// reads in users map from the file

	private void addListeners() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// focus listeners
		username.addFocusListener(new TextFieldFocusListener("username", username));
		password.addFocusListener(new TextFieldFocusListener("password", password));
		// document listeners
		username.getDocument().addDocumentListener(new MyDocumentListener());
		password.getDocument().addDocumentListener(new MyDocumentListener());
		// action listeners
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String usernameString = username.getText();
				String passwordString = String.valueOf(password.getPassword());

				// if the username does not exist
				tryLogin(usernameString, passwordString);
			}

		});
		forgotPassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addReturn(new ForgotPassword(loginGUI));
				setVisible(false);

			}
		});
		createAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String usernameString = username.getText();
				String passwordString = String.valueOf(password.getPassword());
				// if this username has already been chosen
				if (createAccount()) {
					setVisible(false);
					addReturn(new CreateAccount(usernameString, passwordString, loginGUI));
				}

			}

		});
		GuestMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MainGUI(new User("Guest Mode", "fakePassword"), false).setVisible(true);
			}
		});
		visblePasswordCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (visblePasswordCheck.isSelected()) {
					password.setEchoChar((char) 0);
				} else {
					password.setEchoChar('*');
				}

			}
		});
	}

	public boolean createAccount() {
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
			ps.setString(1, username.getText());
			rs = ps.executeQuery();
			if (rs.next()) {
				alertLabel.setText("This username has already been chosen by another user");
				return false;
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException 6: " + sqle.getMessage());
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
		return true;
	}

	private <T extends Object> void addReturn(T frame) {
		if (frame instanceof ForgotPassword) {
			((ForgotPassword) frame).getReturn().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((Window) frame).dispose();
					setVisible(true);
				}
			});
		} else if (frame instanceof CreateAccount) {
			((CreateAccount) frame).getReturn().addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					((CreateAccount) frame).dispose();
					setVisible(true);
				}
			});
		}
	}

	// sets the buttons enabled or disabled
	public class MyDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
	}
}

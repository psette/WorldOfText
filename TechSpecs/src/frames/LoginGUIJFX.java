package frames;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game_logic.User;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginGUIJFX extends JFrame {
	private static CheckBox visblePasswordCheck;
	private static final long serialVersionUID = -6957949909513502626L;
	private static Button loginButton;
	private static Button createAccount;
	private static Button forgotPassword;
	private static Button GuestMode;
	private static TextField username;
	private static PasswordField password;
	private static Label alertLabel, welcome, titleLabel;
	// users map
	// could have use <String, String> instead of User object, but chose not to
	// the file that contains user account info

	public LoginGUIJFX() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// initializeComponents();
				createGUI();
				addListeners();
			}
		});

		// createGUI();

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
			System.out.println("SQLException: " + sqle.getMessage());
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
		JFXPanel fxPanel = new JFXPanel();
		// JPanel textFieldOnePanel = new JPanel();
		// JPanel textFieldTwoPanel = new JPanel();
		// J = new JLabel("Play as guest or login for premium access",
		// JLabel.CENTER);
		// JLabel titleLabel = new JLabel("World of text!", JLabel.CENTER);
		// JPanel alertPanel = new JPanel();
		// JPanel textFieldsPanel = new JPanel();
		// JPanel buttonsPanel = new JPanel();
		// JPanel forgotPanel = new JPanel();
		// JPanel welcomePanel = new JPanel(new GridLayout(2, 1));
		// forgotPanel.add(forgotPassword);
		// forgotPanel.add(visblePasswordCheck);
		// GuestMode.setSize(100, 40);
		// forgotPassword.setSize(100, 40);
		// // set mass component appearances
		// AppearanceSettings.setForeground(Color.black, createAccount,
		// GuestLogin, GuestMode, loginButton, password,
		// username);
		// AppearanceSettings.setSize(500, 60, password, username);
		//
		// AppearanceSettings.setSize(200, 80, loginButton, createAccount);
		// AppearanceSettings.setBackground(Color.yellow, loginButton,
		// GuestMode, createAccount);
		//
		// AppearanceSettings.setOpaque(loginButton, GuestMode, forgotPassword,
		// createAccount);
		// AppearanceSettings.unSetBorderOnButtons(loginButton, GuestMode,
		// forgotPassword, createAccount);
		//
		// AppearanceSettings.setTextAlignment(welcome, alertLabel, titleLabel);
		// AppearanceSettings.setFont(AppearanceConstants.fontSmall, password,
		// alertLabel, username, GuestMode,
		// forgotPassword, loginButton, createAccount, visblePasswordCheck);
		//
		// AppearanceSettings.setBackground(Color.pink, forgotPassword,
		// mainPanel, welcome, alertLabel, titleLabel,
		// alertPanel, forgotPanel, textFieldsPanel, buttonsPanel, welcomePanel,
		// textFieldOnePanel,
		// textFieldTwoPanel, GuestLogin, visblePasswordCheck);
		//
		// // other appearance settings
		// welcome.setFont(AppearanceConstants.fontMedium);
		// titleLabel.setFont(AppearanceConstants.fontLarge);
		// loginButton.setEnabled(false);
		// AppearanceSettings.setBackground(AppearanceConstants.lightBlue,
		// loginButton, createAccount, GuestMode);
		// createAccount.setEnabled(false);
		//
		// // add components to containers
		// textFieldOnePanel.add(username);
		// textFieldTwoPanel.add(password);

		// // adds components to the panel with glue in between each
		// AppearanceSettings.addGlue(buttonsPanel, BoxLayout.LINE_AXIS, true,
		// loginButton, createAccount, GuestMode);
		//
		// AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, false,
		// welcomePanel);
		// // don't want glue in between the following two panels, so they are
		// not
		// // passed in to addGlue
		// setSize(1000, 1000);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Scene scene = createScene();
				// scene.a
				// fxPanel.setPreferredSize(new Dimension(1000,1000));
				fxPanel.setScene(scene);
				// fxPanel.setAlignmentY(500);
				setLayout(new BorderLayout());
				add(fxPanel, BorderLayout.CENTER);
				// fxPanel.setAlignmentY(Pos.CENTER);
				setSize(1000, 1000);
				revalidate();
				repaint();
				pack();
				setResizable(false);

			}
		});
	}

	private static Scene createScene() {
		Group root = new Group();
		welcome = new Label("Welcome!");
		GuestMode = new Button("Play as Guest");
		loginButton = new Button("Login");
		forgotPassword = new Button("Forgot Account?");
		createAccount = new Button("Create Account");
		username = new TextField();
		username.setPromptText("username");
		password = new PasswordField();
		password.setPromptText("password");
		// password.setEchoChar('*');
		alertLabel = new Label();
		titleLabel = new Label("World of text!");
		visblePasswordCheck = new CheckBox("See Password");

		VBox vbox = new VBox(100);

		HBox hbox = new HBox(50);
		hbox.getChildren().addAll(loginButton, createAccount, GuestMode);
		vbox.getChildren().addAll(alertLabel, welcome, titleLabel, username, password, forgotPassword,
				visblePasswordCheck, hbox);
		root.getChildren().add(vbox);
		Scene scene = new Scene(root);// hbox);//new HBox());//(root);//

		vbox.setAlignment(Pos.CENTER);
		hbox.setAlignment(Pos.CENTER);

		return (scene);
	}

	// returns whether the buttons should be enabled
	// private boolean canPressButtons() {
	// return (!username.getText().isEmpty() &&
	// !username.getText().equals("username")
	// && !String.valueOf(password.getPassword()).equals("password")
	// && !String.valueOf(password.getPassword()).isEmpty());
	// }

	// reads in users map from the file

	private void addListeners() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				// // focus listeners
				// username.addFocusListener(new
				// TextFieldFocusListener("username", username));
				// password.addFocusListener(new
				// TextFieldFocusListener("password", password));
				// // document listeners
				// username.getDocument().addDocumentListener(new
				// MyDocumentListener());
				// password.getDocument().addDocumentListener(new
				// MyDocumentListener());
				// // action listeners

				loginButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						String usernameString = username.getText();
						String passwordString = String.valueOf(password.getText());// .getPassword());

						// if the username does not exist
						tryLogin(usernameString, passwordString);
					}
				});

				forgotPassword.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						// addReturn(new ForgotPassword(), this);
						setVisible(false);
					}
				});

				createAccount.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						// String usernameString = username.getText();
						// String passwordString =
						// String.valueOf(password.getText());
						// if this username has already been chosen
						if (createAccount()) {
							// addReturn(new CreateAccount(usernameString,
							// passwordString));
							setVisible(false);
						}
					}
				});

				// createAccount.addActionListener(new ActionListener() {
				//
				// @Override
				// public void actionPerformed(ActionEvent e) {
				//
				//
				// }
				//
				// }
				//
				// });
				// System.out.println(GuestMode);
				GuestMode.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						System.out.println("hhet");
						dispose();
						new MainGUI(new User("Guest Mode", "fakePassword"), false).setVisible(true);
					}
				});

				// visblePasswordCheck.setOnAction(new
				// EventHandler<ActionEvent>(){
				//
				// @Override
				// public void handle(ActionEvent event) {
				// // TODO Auto-generated method stub
				// if (visblePasswordCheck.isSelected()) {
				// password.setEchoChar((char) 0);
				// } else {
				// password.setEchoChar('*');
				// }
				// }});

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
			System.out.println("SQLException: " + sqle.getMessage());
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

	// private <T extends Object> void addReturn(T frame) {
	// if (frame instanceof ForgotPassword) {
	// ((ForgotPassword) frame).getReturn().addActionListener(new
	// ActionListener() {
	// @Override
	// public void actionPerformed(java.awt.event.ActionEvent e) {
	// ((Window) frame).dispose();
	// setVisible(true);
	// }
	// });
	//
	// }
	//
	// else if (frame instanceof CreateAccount) {
	// ((CreateAccount) frame).getReturn().addActionListener(new
	// ActionListener() {
	//
	// @Override
	// public void actionPerformed(java.awt.event.ActionEvent e) {
	// ((CreateAccount) frame).dispose();
	// setVisible(true);
	// }
	// });
	// }
	// }

	// // sets the buttons enabled or disabled
	// public class MyDocumentListener implements DocumentListener {
	//
	// @Override
	// public void insertUpdate(DocumentEvent e) {
	// createAccount.setEnabled(canPressButtons());
	// loginButton.setEnabled(canPressButtons());
	// }
	//
	// @Override
	// public void removeUpdate(DocumentEvent e) {
	// createAccount.setEnabled(canPressButtons());
	// loginButton.setEnabled(canPressButtons());
	// }
	//
	// @Override
	// public void changedUpdate(DocumentEvent e) {
	// createAccount.setEnabled(canPressButtons());
	// loginButton.setEnabled(canPressButtons());
	// }
	// }

	public static void main(String[] args) {

		new LoginGUIJFX().setVisible(true);
	}
}

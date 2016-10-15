package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import game_logic.UsersDB;
import listeners.TextFieldFocusListener;
import other_gui.AppearanceConstants;

public class LoginWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField user = new JTextField(14);
	private JPasswordField password = new JPasswordField(14);
	private JButton login = new JButton("Login"), create = new JButton("Create Account");
	private JLabel alreadyExsits = new JLabel("this usernanme already exsits");
	private UsersDB database;
	private LoginWindow window;

	public LoginWindow(boolean thisOne) {
		super("Login");
		window = this;
		database = new UsersDB();
		user.setForeground(Color.gray);
		password.setForeground(Color.gray);
		user.setText("Username");
		password.setText("Password");
		user.addFocusListener(new TextFieldFocusListener("Username", user));
		password.addFocusListener(new TextFieldFocusListener("Password", password));
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		main.add(AddLoginDescription());
		main.add(AddTitleAndWarning());
		main.add(AddBlankFields());
		main.add(AddButtons());
		add(main);
		setVisible(true);
		setSize(300, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			FileInputStream fis = new FileInputStream("database123123.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			database = (UsersDB) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public LoginWindow() {
		super("Login");
		window = this;
		database = new UsersDB();
		user.setForeground(Color.gray);
		password.setForeground(Color.gray);
		user.setText("Username");
		password.setText("Password");
		user.addFocusListener(new TextFieldFocusListener("Username", user));
		password.addFocusListener(new TextFieldFocusListener("Password", password));
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		main.add(AddLoginDescription());
		main.add(AddTitleAndWarning());
		main.add(AddBlankFields());
		main.add(AddButtons());
		add(main);
		setVisible(true);
		setSize(300, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}

	private JPanel AddLoginDescription() {
		JPanel main = new JPanel();
		main.setBackground(AppearanceConstants.lightBlue);
		JLabel label = new JLabel("login or create an account to play");
		label.setFont(new Font("Courier New", 1, 14));
		main.add(label);
		return main;

	}

	private JPanel AddTitleAndWarning() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		main.setBackground(AppearanceConstants.lightBlue);
		JLabel title = new JLabel("Jeopardy");
		title.setFont(new Font("Courier New", Font.BOLD, 24));
		JPanel temp = new JPanel();
		temp.setBackground(AppearanceConstants.lightBlue);
		temp.add(title);
		main.add(temp);
		temp = new JPanel();
		temp.setBackground(AppearanceConstants.lightBlue);
		alreadyExsits.setFont(new Font("Courier New", Font.BOLD, 16));
		temp.add(alreadyExsits);
		main.add(temp);
		alreadyExsits.setVisible(false);
		return main;
	}

	private JPanel AddBlankFields() {
		JPanel main = new JPanel(), userPanel = new JPanel(), passPanel = new JPanel();
		main.setBackground(AppearanceConstants.lightBlue);
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		userPanel.setBackground(AppearanceConstants.lightBlue);
		user.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				maybeEnable();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		password.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				maybeEnable();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		userPanel.add(user);
		passPanel.add(password);
		passPanel.setBackground(AppearanceConstants.lightBlue);
		main.add(userPanel);
		main.add(passPanel);
		return main;
	}

	private void maybeEnable() {
		if (!user.getText().isEmpty() && !user.getText().equals("Username")
				&& !String.valueOf(password.getPassword()).isEmpty()
				&& !String.valueOf(password.getPassword()).equals("Password")) {
			login.setEnabled(true);
			login.setForeground(Color.WHITE);
			create.setEnabled(true);
			create.setForeground(Color.WHITE);
		}
	}

	private JPanel AddButtons() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(AppearanceConstants.lightBlue);
		login.setBackground(Color.darkGray);
		login.setForeground(Color.LIGHT_GRAY);
		login.setSize(new Dimension(250, 70));
		login.setOpaque(true);
		login.setBorderPainted(false);
		login.setEnabled(false);
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		create.setBackground(Color.darkGray);
		create.setForeground(Color.LIGHT_GRAY);
		create.setSize(new Dimension(250, 70));
		create.setOpaque(true);
		create.setBorderPainted(false);
		create.setEnabled(false);
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				create();
			}
		});
		buttonPanel.add(login);
		buttonPanel.add(create);
		return buttonPanel;
	}

	private void login() {
		try {
			FileInputStream fis = new FileInputStream("database123123.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			database = (UsersDB) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!database.userPassCombo(user.getText(), String.valueOf(password.getPassword()))) {
			alreadyExsits.setText("this password and username combination does not exsit");
			alreadyExsits.setVisible(true);
		} else {
			window.dispose();
			StartWindowGUI startWindow = new StartWindowGUI(this);
			startWindow.setVisible(true);
			Dimension d = startWindow.getSize();
			System.out.println("height: " + d.getHeight() + "; width: " + d.getWidth());
		}
	}

	private void create() {
		if (database.hasUser(user.getText())) {
			alreadyExsits.setVisible(true);
		} else {
			alreadyExsits.setVisible(false);
			database.addUser(user.getText(), String.valueOf(password.getPassword()));
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("database123123.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(database);
				oos.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			user.setText("Username");
			user.setForeground(Color.gray);
			password.setText("Password");
			password.setForeground(Color.gray);
			login.setEnabled(false);
			create.setEnabled(false);
		}
	}
}

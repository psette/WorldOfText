package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

import game_logic.User;
import listeners.TextFieldFocusListener;
import other_gui.AppearanceSettings;
import server.CharacterStylePair;
import server.Client;
import server.RemoveBulk;
import server.RemoveUpdate;
import server.Update;
import server.getPaneInitial;

public class MainGUI extends JFrame implements Serializable {

	private static final long serialVersionUID = 2L;
	private static final String defaultFont = "Times New Roman";
	private String[] fontList, sizeList, colorListStr, fillListStr;
	private String username;
	private SimpleAttributeSet newAttrs = new SimpleAttributeSet();
	private MainGUI gui;
	private JMenuItem logout;
	private JMenuItem loading;
	private JLabel numActiveUsers;
	private JButton italicizeButton, boldButton, underlineButton, clearButton, loginButton, createAccountButton;;
	private JComboBox<String> fontBox, sizeBox, colorBox, fillBox;
	private JPanel mainPanel;
	private boolean isAuthenticated;
	private JTextPane pane;
	private JTextArea messageArea;
	private JTextField chatField;
	private StyledDocument doc;
	private Style[][][][] docStyles;

	private String currFont;
	private Color currColor;
	private Color currFill;
	private boolean bold;
	private boolean italics;
	private boolean underline;
	private int boldCounter;
	private int italicsCounter;
	private int underlineCounter;
	private int currSize;
	public boolean alreadySet;

	public JTextPane getPane() {
		return pane;
	}

	public void setPane(JTextPane pane) {
		this.pane = pane;
	}

	private Client client;
	private int port;
	private String hostname;
	private User user;
	public DocumentListener documentListener;
	public LoadingWindow loadingWindow;

	public MainGUI(User user, boolean isAuthenticated) {
		super("Welcome to World of Text!");
		loadingWindow = new LoadingWindow();
		alreadySet = false;
		gui = this;
		this.user = user;
		if (!isAuthenticated) {
			this.setTitle("Welcome to World of Text! (Guest Mode)");
		}
		username = user.getUsername();
		fontList = new String[] { defaultFont, "Courier", "Arial", "Helvetica", "Comic Sans MS" };
		sizeList = new String[] { "12", "20", "28", "36", "48" };
		colorListStr = new String[] { "black", "blue", "pink", "green", "magenta" };
		fillListStr = new String[] { "white", "pink", "red", "yellow", "cyan" };

		setCurrFont(defaultFont);
		setCurrColor(Color.black);
		setCurrFill(Color.white);
		setBold(false);
		setItalics(false);
		setUnderline(false);
		setCurrSize(12);
		boldCounter = 0;
		italicsCounter = 0;
		underlineCounter = 0;

		this.isAuthenticated = isAuthenticated;

		initializeComponents();
		createGUI();
		addListeners();
	}

	private void initializeComponents() {
		mainPanel = new JPanel();
		add(mainPanel, BorderLayout.CENTER);

		Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
		fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

		italicizeButton = new JButton("I");
		italicizeButton.setFont(new Font(defaultFont, Font.ITALIC, 26));
		boldButton = new JButton("B");
		boldButton.setFont(new Font(defaultFont, Font.BOLD, 26));
		underlineButton = new JButton("U");
		underlineButton.setFont(new Font(defaultFont, Font.PLAIN, 26).deriveFont(fontAttributes));
		clearButton = new JButton("Clear");

		messageArea = new JTextArea("Welcome to the chat!\nStart chatting with your fellas!\n");
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);

		chatField = new JTextField("");
		chatField.addFocusListener(new TextFieldFocusListener("Press 'Enter' to send message", chatField));
		chatField.setEditable(true);
		chatField.setSize(new Dimension(180, 50));
		chatField.setMinimumSize(new Dimension(180, 50));
		chatField.setPreferredSize(new Dimension(180, 50));

		pane = new JTextPane();

		fontBox = new JComboBox<String>(fontList);
		sizeBox = new JComboBox<String>(sizeList);
		colorBox = new JComboBox<String>(colorListStr);
		fillBox = new JComboBox<String>(fillListStr);
		AppearanceSettings.setSize(300, 30, fontBox, sizeBox, colorBox, fillBox);

		JScrollPane paneScrollPane = new JScrollPane(pane);
		paneScrollPane.setMinimumSize(new Dimension(1100, 800));
		paneScrollPane.setPreferredSize(new Dimension(1100, 800));
		paneScrollPane.setMaximumSize(new Dimension(1100, 800));
		paneScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mainPanel.add(paneScrollPane);
		setLayout(new BorderLayout());
		logout = new JMenuItem("Log out");
		loading = new JMenuItem("Check out our Loading Screen!");
		numActiveUsers = new JLabel("", SwingConstants.CENTER);
		setNumActiveUsers(2);

		// for guest mode only
		loginButton = new JButton("Login");
		createAccountButton = new JButton("Create an Account");
		Font font = new Font("Times New Roman", Font.PLAIN, 28);
		AppearanceSettings.setFont(font, loginButton, createAccountButton);
		AppearanceSettings.unSetBorderOnButtons(loginButton, createAccountButton);

		// full screen
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setBackground(Color.black);

		// set client
		client = new Client(port, hostname, user, this);
		client.sendMessageToServer(new getPaneInitial());
		doc = pane.getStyledDocument();
	}

	private void createGUI() {
		add(mainPanel, BorderLayout.CENTER);
		if (isAuthenticated) {
			createMenu();
			createRightPanel();

		} else {
			JMenuBar menu = new JMenuBar();
			JLabel user = new JLabel("Guest Mode");
			user.setFont(new Font("Times New Roman", Font.PLAIN, 18));
			menu.add(user);
			setJMenuBar(menu);

			// add bottom login buttons
			JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
			bottomPanel.setPreferredSize(new Dimension(400, 70));
			bottomPanel.add(loginButton);
			bottomPanel.add(createAccountButton);
			AppearanceSettings.setBackground(Color.pink, bottomPanel, loginButton, createAccountButton, mainPanel);
			add(bottomPanel, BorderLayout.SOUTH);
		}
	}

	private void createRightPanel() {
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel botLeftPanel = new JPanel(new BorderLayout());
		botLeftPanel.setSize(new Dimension(180, 500));
		JPanel functionPanel = new JPanel(new GridLayout(6, 1));
		JPanel southPanel = new JPanel(new BorderLayout());
		JLabel chatLabel = new JLabel("Chat Room", SwingConstants.CENTER);
		Font font = new Font("Comic Sans MS", Font.BOLD, 28);
		AppearanceSettings.setFont(font, chatLabel);
		chatLabel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		rightPanel.setSize(400, 800);
		rightPanel.add(functionPanel, BorderLayout.NORTH);
		add(rightPanel, BorderLayout.EAST);

		AppearanceSettings.setBackground(Color.PINK, chatLabel, numActiveUsers, southPanel, botLeftPanel);

		// add functions to function panel
		functionPanel.add(fontBox); // font name
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
		buttonsPanel.add(italicizeButton);
		buttonsPanel.add(boldButton);
		buttonsPanel.add(underlineButton);
		functionPanel.add(buttonsPanel); // font buttons
		functionPanel.add(sizeBox); // font size
		functionPanel.add(colorBox); // font color
		functionPanel.add(fillBox); // font background color
		functionPanel.add(clearButton); // clear button

		JScrollPane updatesScrollPane = new JScrollPane(messageArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		updatesScrollPane.setBorder(null);
		updatesScrollPane.setPreferredSize(new Dimension(180, 450));

		botLeftPanel.add(chatLabel, BorderLayout.NORTH);
		botLeftPanel.add(updatesScrollPane, BorderLayout.CENTER);
		southPanel.add(chatField, BorderLayout.NORTH);
		southPanel.add(numActiveUsers, BorderLayout.SOUTH);
		botLeftPanel.add(southPanel, BorderLayout.SOUTH);

		// add active user number label to bottom of right panel
		rightPanel.add(botLeftPanel, BorderLayout.SOUTH);

		AppearanceSettings.setBackground(Color.pink, rightPanel, functionPanel, buttonsPanel, clearButton,
				numActiveUsers, mainPanel, updatesScrollPane);
		Font timesNewRoman = new Font(defaultFont, Font.PLAIN, 16);
		AppearanceSettings.setFont(timesNewRoman, fontBox, sizeBox, colorBox, fillBox, clearButton, numActiveUsers);
	}

	// menu bar shows username and allows user to log out
	private void createMenu() {
		JMenuBar menu = new JMenuBar();
		JMenu user = new JMenu("User: " + username);
		user.add(logout);
		user.add(loading);
		menu.add(user);
		setJMenuBar(menu);
	}

	@SuppressWarnings("unused")
	protected void addStylesToDocument(StyledDocument doc) {

		// Style Array references
		docStyles = new Style[2][8][3][3];
		String[] fontFamilies = { "Times New Roman", "Comic Sans MS" };
		String[] fontStyles = { "Regular", "Bold", "Italics", "Underline", "BI", "BU", "IU", "All" };
		String[] fontColors = { "black", "pink", "green" };
		int[] fontSizes = { 6, 18, 36 };

		Color color = null;

		for (int fontFamily = 0; fontFamily < 2; fontFamily++) {
			for (int fontStyle = 0; fontStyle < 8; fontStyle++) {
				for (int fontSize = 0; fontSize < 3; fontSize++) {
					for (int fontColor = 0; fontColor < 3; fontColor++) {
						Style tempStyle = doc.addStyle(null, null);
						StyleConstants.setFontFamily(tempStyle, fontFamilies[fontFamily]); // Font
																							// family
						StyleConstants.setFontSize(tempStyle, fontSizes[fontSize]); // Font
																					// size

						try { // Apply color
							Field field = Class.forName("java.awt.Color").getField(fontColors[fontColor]);
							color = (Color) field.get(null);
						} catch (Exception exc) {
						}

						StyleConstants.setForeground(tempStyle, color);

						if (fontStyle == 0) { // Regular style
							StyleConstants.setBold(newAttrs, false);
							StyleConstants.setItalic(newAttrs, false);
							StyleConstants.setUnderline(newAttrs, false);
						} else if (fontStyle == 1) {// Bold style
							StyleConstants.setBold(newAttrs, true);
							StyleConstants.setItalic(newAttrs, false);
							StyleConstants.setUnderline(newAttrs, false);
						} else if (fontStyle == 2) {// Italics style
							StyleConstants.setBold(newAttrs, false);
							StyleConstants.setItalic(newAttrs, true);
							StyleConstants.setUnderline(newAttrs, false);
						} else if (fontStyle == 3) {// Underline style
							StyleConstants.setBold(newAttrs, false);
							StyleConstants.setItalic(newAttrs, false);
							StyleConstants.setUnderline(newAttrs, true);
						} else if (fontStyle == 4) {// Bold & Italics style
							StyleConstants.setBold(newAttrs, true);
							StyleConstants.setItalic(newAttrs, true);
							StyleConstants.setUnderline(newAttrs, false);
						} else if (fontStyle == 5) {// Bold & Underline style
							StyleConstants.setBold(newAttrs, true);
							StyleConstants.setItalic(newAttrs, false);
							StyleConstants.setUnderline(newAttrs, true);
						} else if (fontStyle == 6) {// Italics & Underline style
							StyleConstants.setBold(newAttrs, false);
							StyleConstants.setItalic(newAttrs, true);
							StyleConstants.setUnderline(newAttrs, true);
						} else { // All three style
							StyleConstants.setBold(newAttrs, true);
							StyleConstants.setItalic(newAttrs, true);
							StyleConstants.setUnderline(newAttrs, true);
						}

						docStyles[fontFamily][fontStyle][fontSize][fontColor] = tempStyle;
					}
				}
			}
		}

	}

	public void setNumActiveUsers(int num) {
		if (num == 1) {
			numActiveUsers.setText("1 active user");
		} else {
			numActiveUsers.setText(num + " active users");
		}
	}

	private void addListeners() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pane.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (!isAuthenticated
						&& (e.getModifiers() == InputEvent.META_MASK || e.isControlDown() || e.isShiftDown())) {
					JOptionPane.showMessageDialog(gui, "Guest users cannot select control.");
				}

			}
		});
		if (!isAuthenticated) {
			pane.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if (pane.getSelectedText() != null) {
						JOptionPane.showMessageDialog(gui, "Guest users cannot select multiple text fields.");
						pane.setCaretPosition(0);
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// Auto-generated method stub

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// Auto-generated method stub

				}
			});
		}
		documentListener = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				int start = e.getOffset();
				if (e.getLength() == 0) {
					client.sendMessageToServer(new RemoveUpdate(start));
				} else {
					client.sendMessageToServer(new RemoveBulk(start, e.getLength()));
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				int start = e.getOffset();
				pane.getText();
				client.sendMessageToServer(
						new Update(pane.getText().charAt(start), start, doc.getCharacterElement(start), "insert"));

			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}

		};
		pane.getDocument().addDocumentListener(documentListener);

		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// logout client
				client.closeStreams();
				client.interrupt();
				new LoginGUI().setVisible(true);
				dispose();
			}
		});
		loading.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadingWindow.setVisible(true);
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel = new JPanel();
			}
		});

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginGUI().setVisible(true);
				dispose();
			}
		});

		createAccountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginGUI().setVisible(true);
				dispose();
			}
		});

		italicizeButton.addActionListener(new StyledEditorKit.ItalicAction());
		italicizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				italicsCounter++;
				if (italicsCounter % 2 == 0)
					setItalics(false);
				if (italicsCounter % 2 == 1)
					setItalics(true);
				if (pane.getSelectedText() != null) {
					SimpleAttributeSet newAttrs = new SimpleAttributeSet();
					StyleConstants.setItalic(newAttrs, italics);
					client.sendMessageToServer(
							new Update(pane.getSelectionStart(), pane.getSelectedText().length(), newAttrs, "edit"));
				}
			}
		});

		boldButton.addActionListener(new StyledEditorKit.BoldAction());
		boldButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boldCounter++;
				if (boldCounter % 2 == 0)
					setBold(false);
				if (boldCounter % 2 == 1)
					setBold(true);
				if (pane.getSelectedText() != null) {
					SimpleAttributeSet newAttrs = new SimpleAttributeSet();
					StyleConstants.setBold(newAttrs, bold);
					client.sendMessageToServer(
							new Update(pane.getSelectionStart(), pane.getSelectedText().length(), newAttrs, "edit"));
				}
			}
		});

		underlineButton.addActionListener(new StyledEditorKit.UnderlineAction());
		underlineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				underlineCounter++;
				if (underlineCounter % 2 == 0)
					setUnderline(false);
				if (underlineCounter % 2 == 1)
					setUnderline(true);
				if (pane.getSelectedText() != null) {
					SimpleAttributeSet newAttrs = new SimpleAttributeSet();
					StyleConstants.setUnderline(newAttrs, underline);
					client.sendMessageToServer(
							new Update(pane.getSelectionStart(), pane.getSelectedText().length(), newAttrs, "edit"));
				}
			}
		});

		fontBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String fontString = fontBox.getItemAt(fontBox.getSelectedIndex());
				if (fontBox.getActionListeners().length != 0) {
					fontBox.removeActionListener(fontBox.getActionListeners()[0]);
				}
				fontBox.addActionListener(new StyledEditorKit.FontFamilyAction(fontString, fontString));
				setCurrFont(fontString);
				if (pane.getSelectedText() != null) {
					SimpleAttributeSet newAttrs = new SimpleAttributeSet();
					StyleConstants.setFontFamily(newAttrs, currFont);
					client.sendMessageToServer(
							new Update(pane.getSelectionStart(), pane.getSelectedText().length(), newAttrs, "edit"));
				}
			}
		});
		sizeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String sizeString = sizeBox.getItemAt(sizeBox.getSelectedIndex());
				Integer newSize = Integer.parseInt(sizeBox.getItemAt(sizeBox.getSelectedIndex()));
				if (sizeBox.getActionListeners().length != 0) {
					sizeBox.removeActionListener(sizeBox.getActionListeners()[0]);
				}
				sizeBox.addActionListener(new StyledEditorKit.FontSizeAction(sizeString, newSize));
				setCurrSize(newSize);
				if (pane.getSelectedText() != null) {
					SimpleAttributeSet newAttrs = new SimpleAttributeSet();
					StyleConstants.setFontSize(newAttrs, currSize);
					client.sendMessageToServer(
							new Update(pane.getSelectionStart(), pane.getSelectedText().length(), newAttrs, "edit"));
				}
			}
		});

		colorBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String colorString = colorBox.getItemAt(colorBox.getSelectedIndex());
				if (colorBox.getActionListeners().length != 0) {
					colorBox.removeActionListener(colorBox.getActionListeners()[0]);
				}

				Color color;
				try {
					Field field = Class.forName("java.awt.Color").getField(colorString);
					color = (Color) field.get(null);
				} catch (Exception exc) {
					color = null; // Not defined
				}

				colorBox.addActionListener(new StyledEditorKit.ForegroundAction(colorString, color));
				if (pane.getSelectedText() != null) {
					SimpleAttributeSet newAttrs = new SimpleAttributeSet();
					StyleConstants.setForeground(newAttrs, color);
					client.sendMessageToServer(
							new Update(pane.getSelectionStart(), pane.getSelectedText().length(), newAttrs, "edit"));
				}
			}
		});

		fillBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String fillString = fillBox.getItemAt(fillBox.getSelectedIndex());
				if (fillBox.getActionListeners().length != 0) {
					fillBox.removeActionListener(fillBox.getActionListeners()[0]);
				}

				Color color;
				try {
					Field field = Class.forName("java.awt.Color").getField(fillString);
					color = (Color) field.get(null);
				} catch (Exception exc) {
					color = null; // Not defined
				}

				setCurrFill(color);
				pane.setBackground(color);
			}
		});

		chatField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = chatField.getText();
				client.writeMessage(message);
				chatField.setText("");
			}
		});

	}

	public void updateMessageArea(String msg) {
		if (msg.startsWith(user.getUsername())) {
			msg = "YOU" + msg.substring(user.getUsername().length());
		}
		messageArea.append(msg + "\n");

		messageArea.setCaretPosition(messageArea.getDocument().getLength());
	}

	public String getCurrFont() {
		return currFont;
	}

	public void setCurrFont(String currFont) {
		this.currFont = currFont;
	}

	public Color getCurrColor() {
		return currColor;
	}

	public void setCurrColor(Color currColor) {
		this.currColor = currColor;
	}

	public Color getCurrFill() {
		return currFill;
	}

	public void setCurrFill(Color currFill) {
		this.currFill = currFill;
	}

	public int getCurrSize() {
		return currSize;
	}

	public void setCurrSize(int currSize) {
		this.currSize = currSize;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public boolean isItalics() {
		return italics;
	}

	public void setItalics(boolean italics) {
		this.italics = italics;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public Vector<CharacterStylePair> getPaneVec() {
		String text = pane.getText();
		StyledDocument doc = pane.getStyledDocument();
		Vector<CharacterStylePair> list = new Vector<CharacterStylePair>();
		for (int i = 0; i < text.length(); ++i) {
			char ch = text.charAt(i);
			Element el = doc.getCharacterElement(i);
			list.add(new CharacterStylePair(ch, el));
		}
		return list;
	}

	public void initializeText(Vector<CharacterStylePair> content) {
		StyledDocument doc = pane.getStyledDocument();
		for (int i = 0; i < content.size(); ++i) {
			try {
				doc.insertString(i, String.valueOf(content.get(i).getCharacter()),
						content.get(i).getElement().getAttributes());
			} catch (BadLocationException e) {
				System.out.println("BadLocationException: " + e.getMessage());
			}
		}
	}

}

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.text.BadLocationException;

import frames.MainGUI;
import game_logic.User;

public class Client extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket;
	private User user;
	private MainGUI mainGUI;
	private ChatMessage cm;

	// Constructor for reading messages
	public Client(int port, String hostname, User user, MainGUI mainGUI) {
		port = 6789;
		hostname = "localhost";
		socket = null;
		try {
			socket = new Socket(hostname, port);

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			this.user = user;
			this.mainGUI = mainGUI;
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe from client: " + ioe.getMessage());
		}
	}

	public void writeMessage(String message) {
		try {
			ChatMessage cm = new ChatMessage(user.getUsername(), message);
			oos.writeObject(cm);
			oos.flush();
			oos.reset();
		} catch (IOException ioe) {
			System.out.println("ioe client msg writer: " + ioe.getMessage());
		}
	}

	public void sendMessageToServer(NetworkMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe in client send msg to server: ");
			ioe.printStackTrace();
		}
	}

	// Listen for new messages from other users
	@Override
	public void run() {
		try {
			while (true) {
				NetworkMessage m = (NetworkMessage) ois.readObject();
				if (m != null) {
					if (m instanceof NumUserChangeMessage) {
						NumUserChangeMessage numChangeMsg = (NumUserChangeMessage) m;
						mainGUI.setNumActiveUsers(numChangeMsg.getNumUsers());
					} else if (!mainGUI.alreadySet) {
						if (m instanceof SendPaneInitial) {
							mainGUI.loadingWindow.setVisible(true);
							mainGUI.setEnabled(false);
							mainGUI.alreadySet = true;
							mainGUI.getPane().getDocument().removeDocumentListener(mainGUI.documentListener);
							mainGUI.initializeText(((SendPaneInitial) m).getContent());
							mainGUI.getPane().getDocument().addDocumentListener(mainGUI.documentListener);
							mainGUI.setEnabled(true);
							mainGUI.loadingWindow.setVisible(false);
							continue;
						} else if (m instanceof FailConnecting) {
							mainGUI.loadingWindow.setVisible(true);
							mainGUI.setEnabled(false);
							String init = "";
							for (int i = 0; i < 50; i++) {
								for (int j = 0; j < 200; j++) {
									init += " ";
								}
								init += "\n";
							}
							mainGUI.alreadySet = true;
							mainGUI.getPane().getDocument().removeDocumentListener(mainGUI.documentListener);
							mainGUI.getPane().setText(init);
							mainGUI.getPane().getDocument().addDocumentListener(mainGUI.documentListener);
							mainGUI.setEnabled(true);
							mainGUI.loadingWindow.setVisible(false);
							continue;
						}
					} else {
						if (m instanceof ChatMessage) {
							cm = (ChatMessage) m;
							String msg = cm.getUsername() + ": " + cm.getMessage();
							mainGUI.updateMessageArea(msg);
						} else if (m instanceof Update) {
							Update update = (Update) m;

							if (update.getUpdateType().equals("insert")) {

								mainGUI.getPane().getDocument().removeDocumentListener(mainGUI.documentListener);
								mainGUI.getPane().getDocument().insertString(update.getPosition(),
										String.valueOf(update.getCharacter()), update.getCharacterAttribute());
								mainGUI.getPane().getDocument().addDocumentListener(mainGUI.documentListener);

							} else if (update.getUpdateType().equals("edit")) {

								mainGUI.getPane().getStyledDocument().setCharacterAttributes(update.getStartPosition(),
										update.getLength(), update.getCharacterAttribute(), false);

							}

						} else if (m instanceof RemoveUpdate) {
							StringBuilder newPane = new StringBuilder(mainGUI.getPane().getText());
							newPane.deleteCharAt(((RemoveUpdate) m).getRemoveHere());
							mainGUI.getPane().getDocument().removeDocumentListener(mainGUI.documentListener);
							mainGUI.getPane().setText(newPane.toString());
							mainGUI.getPane().getDocument().addDocumentListener(mainGUI.documentListener);
						} else if (m instanceof RemoveBulk) {
							RemoveBulk message = (RemoveBulk) m;
							String newPane = mainGUI.getPane().getText();
							int startIndex = message.getStart();
							int endIndex = startIndex + message.getOffset();
							if (startIndex == 0 && (endIndex >= newPane.length() || endIndex < 0)) {
								newPane = "";
							} else if (startIndex == 0) {
								newPane = newPane.substring(endIndex);
							} else if (endIndex == newPane.length()) {
								newPane = newPane.substring(0, startIndex);
							} else {
								newPane = newPane.substring(0, startIndex) + newPane.substring(endIndex);
							}
							mainGUI.getPane().setText(newPane);

						} else if (m instanceof getPaneInitial) {
							if (mainGUI.alreadySet) {
								sendMessageToServer(new SendPaneInitial(mainGUI.getPaneVec()));
							}
						}
					}

				}
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("ioe client read msg: " + ioe.getMessage());
		} catch (BadLocationException ble) {
			System.out.println("Bad location: " + ble.getMessage());
		} finally {
			closeStreams();
		}
	}

	public void closeStreams() {
		try {
			if (oos != null)
				oos.close();
			if (ois != null)
				ois.close();
			if (socket != null)
				socket.close();
		} catch (IOException ioe) {
			System.out.println("ioe client: " + ioe.getMessage());
		}
	}
}

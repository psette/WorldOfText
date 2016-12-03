package client.sockets;

import actions.ActionFactory;
import client.Constants;
import client.FactoryClientGUI;
import client.game.FactoryManager;
import messages.Message;
import resource.Factory;
import utilities.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/* NOTES
FactoryClientListener: The client's thread that receives Factory from the server. Contains:
- socket:
- factoryManager:
- factoryClientGUI: which we'll add an messages
 */

public class FactoryClientListener extends Thread {

	private Socket socket;
	private ObjectInputStream ois;
	private PrintWriter pw;
	private FactoryManager factoryManager;
	private FactoryClientGUI factoryClientGUI;
	private ActionFactory actionFactory;

	public void sendMessage(Message message) {
		pw.println(message);
		pw.flush();
	}

	public FactoryClientListener(FactoryManager inFManager, FactoryClientGUI inFClientGUI, Socket inSocket) {
		actionFactory = new ActionFactory();
		socket = inSocket;
		factoryManager = inFManager;
		factoryClientGUI = inFClientGUI;
		boolean socketReady = initializeVariables();
		if (socketReady) {
			start();
		}
	}

	private boolean initializeVariables() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			pw = new PrintWriter(socket.getOutputStream());
		} catch (IOException ioe) {
			Util.printExceptionToCommand(ioe);
			Util.printMessageToCommand(Constants.unableToGetStreams);
			return false;
		}
		return true;
	}

	public void setFactory(Factory factory) {
		factoryManager.loadFactory(factory, factoryClientGUI.getTable());
		factoryClientGUI.addMessage(Constants.factoryReceived);
		factoryClientGUI.addMessage(factory.toString());
	}

	public void run() {
		try {
			factoryClientGUI.addMessage(Constants.waitingForFactoryConfigMessage);
			while (true) {
				// in case the server sends another factory to us
				Message message = (Message) ois.readObject();
				// process the message and execute the correct action!
				actionFactory.getAction(message.getClass()).executeAction(factoryClientGUI, factoryManager, message,
						this);
			}
		} catch (IOException ioe) {
			factoryClientGUI.addMessage(Constants.serverCommunicationFailed);
		} catch (ClassNotFoundException cnfe) {
			Util.printExceptionToCommand(cnfe);
		}
	}
}

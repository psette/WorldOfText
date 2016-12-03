package server.sockets;

import messages.FactoryMessage;
import messages.Message;
import resource.Factory;
import server.Constants;
import server.FactoryServerGUI;
import utilities.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

/* NOTES
 EQUIVALENT TO SERVERTHREAD
ServerClientCommunicator holds a single client's socket
- this permits you to send and receive messages from that specific client

METHODS
- sendFactory is called by ServerListener (who sends the same factory object to each client)
- run() reads messages from the socket, and prints them to the GUI
    - if the socket throws an exception, it closes (and removes itself)
 */

class ServerClientCommunicator extends Thread {

	private Socket socket; // java.net.Socket
	private ObjectOutputStream oos;
	private BufferedReader br;
	private ServerListener serverListener;

	// send factory to this specific client
	void sendFactory(Factory factory) {
		try {
			oos.writeObject(new FactoryMessage(factory));
			oos.flush();
		} catch (IOException ioe) {
			Util.printExceptionToCommand(ioe);
		}
	}

	void sendMessage(Message message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException ioe) {
			Util.printExceptionToCommand(ioe);
		}
	}

	ServerClientCommunicator(Socket socket, ServerListener serverListener) throws IOException {
		this.socket = socket;
		this.serverListener = serverListener;
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void run() {
		try {
			String line = br.readLine();
			while (line != null) {
				FactoryServerGUI.addMessage(socket.getInetAddress() + ":" + socket.getPort() + " - " + line);
				line = br.readLine();
			}
		} catch (IOException ioe) {
			serverListener.removeServerClientCommunicator(this);
			FactoryServerGUI.addMessage(
					socket.getInetAddress() + ":" + socket.getPort() + " - " + Constants.clientDisconnected);
			// this means that the socket is closed since no more lines are
			// being received
			try {
				socket.close();
			} catch (IOException ioe1) {
				Util.printExceptionToCommand(ioe1);
			}
		}
	}
}

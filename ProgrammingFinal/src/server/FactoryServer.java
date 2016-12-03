package server;

import resource.Factory;
import server.sockets.PortGUI;
import server.sockets.ServerListener;

import java.net.ServerSocket;
import java.util.ArrayList;

/* NOTES
This is the entry point for the server. It contains:
- ServerListener (Thread): holds an instance of ServerSocket and each client session (sends factory to each client)
- ServerSocket: a java socket

METHODS
.sendFactory: sends to factory to all clients
 */

public class FactoryServer {

	private static ServerListener serverListener;
	private ServerSocket ss;
	public static ArrayList<String> orders = new ArrayList<String>();

	// sends factory to each client
	static void sendFactory(Factory factory) {
		if (serverListener != null) {
			serverListener.sendFactory(factory);
		}
	}

	public FactoryServer() {
		PortGUI pf = new PortGUI(); // for user to enter a port#
		ss = pf.getServerSocket(); // (synchronized) waits for the user to enter
									// a port
		new FactoryServerGUI(); // creates the Factory GUI
		listenForConnections(); // starts the server listener to wait for new
								// clients
	}

	private void listenForConnections() {
		serverListener = new ServerListener(ss);
		serverListener.start();
	}

	synchronized public static void addOrder(String workerName, String time) {
		System.out.println("Adding order");
		String adding = workerName + " ordered coffe at " + time;
		orders.add(adding);
	}

	public static void main(String[] args) {
		new FactoryServer();
	}
}

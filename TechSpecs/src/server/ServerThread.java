package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket;
	private Server server;

	public ServerThread(Socket s, Server serv) {
		try {
			socket = s;
			server = serv;
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe from server thread constructor: " + ioe.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Object o = ois.readObject();
				if (o != null) {
					if (o instanceof ChatMessage) {
						server.distributeChatMessage(o);
					} else if (o instanceof Update) {
						server.update((Update) o, this);
					} else if (o instanceof RemoveUpdate) {
						server.update((RemoveUpdate) o, this);
					} else if (o instanceof RemoveBulk) {
						server.update((RemoveBulk) o, this);
					} else if (o instanceof getPaneInitial) {
						server.pollForUpdate((NetworkMessage) o, this);
					} else if (o instanceof SendPaneInitial) {
						server.instantiateLateLosers((NetworkMessage) o);
					}
				}
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} catch (IOException ioe) {
			server.removeLoggedOutUser(this);
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (oos != null)
					oos.close();
				if (socket != null)
					socket.close();
			} catch (IOException ioe) {
				System.out.println("ioe from server thread: " + ioe.getMessage());
			}
		}
	}

	public void sendMessage(NetworkMessage m) {
		try {
			oos.writeObject(m);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe sendMessage: " + ioe.getMessage());
		}
	}

	public void sendObject(Object o) {
		try {
			oos.writeObject(o);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe from server thread send obj: " + ioe.getMessage());
		}
	}

}

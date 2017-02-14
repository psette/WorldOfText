package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.text.AbstractDocument;

public class Server extends Thread {

	private Vector<ServerThread> stVec;
	private Vector<ServerThread> needsToBeUpdated;
	private int numConnected;
	private int port;
	private ServerSocket ss;
	private AbstractDocument currDoc;
	private String content;

	public Server(int port) {
		needsToBeUpdated = new Vector<ServerThread>();
		System.out.println("Server starting");
		numConnected = 0;
		stVec = new Vector<ServerThread>();
		this.setPort(port);

		try {
			ss = new ServerSocket(port); // No other program is bound to this
											// port
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe from server: " + ioe.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket s = ss.accept(); // Waiting state (waiting for a
										// connection)
				ServerThread st = new ServerThread(s, this);
				numConnected++;
				stVec.add(st);
				distributeNumActiveUser();
			}
		} catch (IOException ioe) {
			System.out.println("ioe server: " + ioe.getMessage());
		} finally {
			try {
				ss.close();
			} catch (IOException ioe) {
				System.out.println("ioe closing server ss: " + ioe.getMessage());
			}
		}
	}

	public void setCurrAbstractDocument(AbstractDocument document) {
		setCurrDoc(document);
	}

	public int getNumConnected() {
		return numConnected;
	}

	public void setNumConnected(int numConnected) {
		this.numConnected = numConnected;
	}

	public void distributeChatMessage(Object message) {
		for (ServerThread st : stVec) {
			st.sendObject(message);
		}
	}

	public void update(NetworkMessage m, ServerThread thread) {
		synchronized (this) {
			for (int i = 0; i < stVec.size(); ++i) {
				ServerThread st = stVec.get(i);
				if (st == thread) {
					continue;
				} else {
					st.sendMessage(m);
				}
			}
		}
	}

	public void removeLoggedOutUser(ServerThread loggedoutThread) {
		for (int i = 0; i < stVec.size(); ++i) {
			ServerThread st = stVec.get(i);
			if (st.equals(loggedoutThread)) {
				stVec.remove(st);
				numConnected--;
				distributeNumActiveUser();
				break;
			}
		}
	}

	public void distributeNumActiveUser() {
		NumUserChangeMessage numChanged = new NumUserChangeMessage(numConnected);
		for (ServerThread stRest : stVec) {
			stRest.sendObject(numChanged);
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void pollForUpdate(NetworkMessage message, ServerThread serverThread) {
		needsToBeUpdated.add(serverThread);
		int i = 0;
		for (; i < stVec.size(); ++i) {
			ServerThread st = stVec.get(i);
			if (needsToBeUpdated.contains(st)) {
				continue;
			} else {
				st.sendMessage(new getPaneInitial());
				break;
			}
		}
		if (i == stVec.size()) {
			needsToBeUpdated.get(0).sendMessage(new FailConnecting());
			needsToBeUpdated.remove(0);
		}

	}

	public void instantiateLateLosers(NetworkMessage o) {
		for (int i = 0; i < needsToBeUpdated.size(); ++i) {
			ServerThread st = needsToBeUpdated.get(i);
			st.sendMessage(o);
			needsToBeUpdated.remove(st);
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public AbstractDocument getCurrDoc() {
		return currDoc;
	}

	public void setCurrDoc(AbstractDocument currDoc) {
		this.currDoc = currDoc;
	}

}

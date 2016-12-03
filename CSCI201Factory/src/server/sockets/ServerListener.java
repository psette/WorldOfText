package server.sockets;

import resource.Factory;
import server.Constants;
import server.FactoryServerGUI;
import utilities.Util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/* NOTES
A single instance of the ServerListener thread is held by FactoryServer.
- holds a vector of ServerClientCommunicators (each client session)
- sends Factory to each client communicator

 */

public class ServerListener extends Thread {

    private ServerSocket ss;
    private Vector<ServerClientCommunicator> sccVector;
    private Factory factory;

    public ServerListener(ServerSocket ss) {
        this.ss = ss;
        sccVector = new Vector<>();
    }

    // this is called by FactoryServer
    public void sendFactory(Factory factory) {
        this.factory = factory;
        for (ServerClientCommunicator scc : sccVector) {
            scc.sendFactory(factory);
        }
    }

    // this is called by ServerClientCommunicator when the client closes its session
    void removeServerClientCommunicator(ServerClientCommunicator scc) {
        sccVector.remove(scc);
    }

    public void run() {
        try {
            FactoryServerGUI.addMessage(Constants.initialFactoryTextAreaString + ss.getLocalPort());
            while (true) {
                Socket s = ss.accept();
                FactoryServerGUI.addMessage(Constants.startClientConnectedString + s.getInetAddress() + Constants.endClientConnectedString);

                try {
                    // this line can throw an IOException
                    // if it does, we won't start the thread
                    ServerClientCommunicator scc = new ServerClientCommunicator(s, this);
                    scc.start();
                    sccVector.add(scc);

                    // right when a client connects, if there is already a factory loaded on the server, send it out
                    if (factory != null) {
                        scc.sendFactory(factory);
                    }
                } catch (IOException ioe) {
                    Util.printExceptionToCommand(ioe);
                }
            }
        } catch (IOException ioe) {
            Util.printExceptionToCommand(ioe);
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException ioe) {
                    Util.printExceptionToCommand(ioe);
                }
            }
        }
    }
}

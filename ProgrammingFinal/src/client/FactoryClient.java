package client;

import client.sockets.HostAndPortGUI;

import java.net.Socket;

/* NOTES
- FactoryClient is point of entry for clients
- first displays the HostAndPortGUI, which will create a socket (waiting...)
- when we receive the socket, we initialize the FactoryClientGUI with it.
 */

public class FactoryClient {

	public FactoryClient() {
        Socket socket = new HostAndPortGUI().getSocket();
        new FactoryClientGUI(socket);
    }

    public static void main(String[] args) {
        new FactoryClient();
    }
}

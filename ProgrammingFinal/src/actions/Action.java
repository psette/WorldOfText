package actions;

import client.FactoryClientGUI;
import client.game.FactoryManager;
import client.sockets.FactoryClientListener;
import messages.Message;

public abstract class Action {
    public abstract void executeAction(
            FactoryClientGUI clientGUI,
            FactoryManager manager,
            Message message,
            FactoryClientListener clientListener);
}

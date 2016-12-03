package actions;

import client.FactoryClientGUI;
import client.game.FactoryManager;
import client.sockets.FactoryClientListener;
import messages.FactoryMessage;
import messages.Message;

public class FactoryAction extends Action {

    @Override
    public void executeAction(FactoryClientGUI clientGUI, FactoryManager manager, Message message, FactoryClientListener clientListener) {
        FactoryMessage msg = (FactoryMessage) message;
        clientListener.setFactory(msg.getFactory());
    }
}

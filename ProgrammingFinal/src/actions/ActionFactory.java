package actions;

import messages.FactoryMessage;

import java.util.HashMap;

public class ActionFactory {

    private static HashMap<Class<?>, Action> actionMap;

    public ActionFactory() {

        if (actionMap == null) {
            actionMap = new HashMap<>();
            actionMap.put(FactoryMessage.class, new FactoryAction());
        }
    }

    public Action getAction(Class<?> messageClass) {
        return actionMap.get(messageClass);
    }
}

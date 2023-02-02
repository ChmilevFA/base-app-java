package net.chmilevfa.templates.action;

import java.util.Map;
import java.util.function.Supplier;

public class MapBasedActionFactory implements ActionFactory {

    private final Map<Class<? extends Action<?, ?>>, Supplier<Action<?, ?>>> actionCreators;

    public MapBasedActionFactory(Map<Class<? extends Action<?, ?>>, Supplier<Action<?, ?>>> actionCreators) {
        this.actionCreators = actionCreators;
    }

    @Override
    public Action<?, ?> createAction(Class<? extends Action<?, ?>> actionClass) {
        final var actionCreator = actionCreators.get(actionClass);
        if (actionCreator == null) {
            throw new IllegalStateException("Action " + actionClass.getSimpleName() + " is not mapped");
        }

        return actionCreator.get();
    }
}

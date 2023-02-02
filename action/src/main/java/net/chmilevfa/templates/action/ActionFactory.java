package net.chmilevfa.templates.action;

@FunctionalInterface
public interface ActionFactory {

    Action<?, ?> createAction(Class<? extends Action<?, ?>> actionClass);
}

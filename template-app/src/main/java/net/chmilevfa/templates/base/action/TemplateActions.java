package net.chmilevfa.templates.base.action;

import net.chmilevfa.templates.action.Action;
import net.chmilevfa.templates.action.Actions;
import net.chmilevfa.templates.action.MapBasedActionFactory;
import net.chmilevfa.templates.base.repository.TemplateRepositories;

import java.time.Clock;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Map.entry;

public class TemplateActions {

    private final TemplateRepositories repositories;
    public final Actions actionFactory;

    public TemplateActions(TemplateRepositories repositories) {
        this.repositories = repositories;
        final var actionCreators = Map.<Class<? extends Action<?, ?>>, Supplier<Action<?, ?>>>ofEntries(
            entry(UserCreateAction.class, this::newUserCreateAction)
        );
        this.actionFactory = new Actions(Clock.systemUTC(), repositories.repositoryRouter, new MapBasedActionFactory(actionCreators));
    }

    private UserCreateAction newUserCreateAction() {
        return new UserCreateAction(repositories.userRepository);
    }

}

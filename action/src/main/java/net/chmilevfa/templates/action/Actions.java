package net.chmilevfa.templates.action;

import net.chmilevfa.templates.repository.Batch;
import net.chmilevfa.templates.repository.RepositoryRouter;
import net.chmilevfa.templates.repository.exception.OptimisticLockingRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.util.function.Supplier;

import static java.time.Duration.between;

@SuppressWarnings({"unchecked"})
public class Actions {

    private static final Logger LOG = LoggerFactory.getLogger(Actions.class);

    private final Clock clock;
    private final RepositoryRouter repositoryRouter;
    private final ActionFactory actionFactory;

    public Actions(Clock clock, RepositoryRouter repositoryRouter, ActionFactory actionFactory) {
        this.clock = clock;
        this.repositoryRouter = repositoryRouter;
        this.actionFactory = actionFactory;
    }

    public <A extends Action<RQ, RS>, RQ, RS> Action<RQ, RS> create(Class<A> actionClass) {
        return new ActionProxy<>(() -> (Action<RQ, RS>) actionFactory.createAction(actionClass));
    }

    private class ActionProxy<RQ, RS> extends Action<RQ, RS> {

        private final Supplier<Action<RQ, RS>> actionCreator;
        private Action<RQ, RS> action;

        public ActionProxy(Supplier<Action<RQ, RS>> actionCreator) {
            this.actionCreator = actionCreator;
            this.action = actionCreator.get();
        }

        @Override
        public RS run(RQ request) {
            RS result;
            final var startTime = clock.instant();
            while (true) {
                try {
                    result = executeAction(request);
                    break;
                } catch (OptimisticLockingRepositoryException e) {
                    if (shouldRetry(startTime)) {
                        LOG.info("Retrying {}", action.name());
                        this.action = actionCreator.get();
                        continue;
                    }
                    throw e;
                }
            }

            return result;
        }

        @Override
        public String name() {
            return action.name();
        }

        @Override
        public Batch changes() {
            return action.changes();
        }

        private boolean shouldRetry(Instant startTime) {
            return between(startTime, clock.instant()).compareTo(action.retryPeriod()) <= 0;
        }

        private RS executeAction(RQ request) {
            final RS result = action.run(request);
            repositoryRouter.processBatch(action.changes());
            return result;
        }
    }
}

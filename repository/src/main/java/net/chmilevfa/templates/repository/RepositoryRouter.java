package net.chmilevfa.templates.repository;


import net.chmilevfa.templates.repository.connection.TransactionManager;
import net.chmilevfa.templates.repository.model.Entity;

import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RepositoryRouter {

    private final TransactionManager transactionManager;
    private final Map<Class<? extends Entity>, Repository> repositories;

    public RepositoryRouter(TransactionManager transactionManager,
                            Map<Class<? extends Entity>, Repository> repositories) {
        this.transactionManager = transactionManager;
        this.repositories = repositories;
    }

    public void processBatch(Batch batch) {
        transactionManager.inTransaction(() -> processBatchInternal(batch));
    }

    private void processBatchInternal(Batch batch) {
        batch.close();
        batch.forEach(change -> {
            final var repository = repositoryFor(change.entity.id().type);
            switch (change.type) {
                case ADD -> repository.add(change.entity);
                case UPDATE -> repository.update(change.entity);
                case DELETE -> repository.delete(change.entity);
            }
        });
    }

    private <E extends Entity<E, ID>, ID extends Comparable<ID>> Repository<E, ID> repositoryFor(Class<E> type) {
        final var repository = repositories.get(type);
        if (repository == null) {
            throw new IllegalStateException("Repository for entity " + type.getSimpleName() + " is not configured");
        }

        return (Repository<E, ID>) repository;
    }
}

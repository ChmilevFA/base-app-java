package net.chmilevfa.templates.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E, ID> {

    E add(E entry);

    E get(ID id);

    Optional<E> find(ID id);

    List<E> findAll();

    E update(E entry);

    boolean exists(ID id);

    boolean delete(E entity);
}

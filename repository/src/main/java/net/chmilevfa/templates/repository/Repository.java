package net.chmilevfa.templates.repository;

import java.util.List;

public interface Repository<E, ID> {

    E add(E entry);

    E get(ID id);

    List<E> findAll();

    E update(E entry);

    boolean exists(ID id);

    boolean delete(ID id);
}

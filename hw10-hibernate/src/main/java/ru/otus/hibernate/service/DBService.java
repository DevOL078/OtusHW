package ru.otus.hibernate.service;

import java.util.List;

public interface DBService<V> {

    void save(V value);

    V get(long id);

    default List<V> getAll() {
        return null;
    }
}

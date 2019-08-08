package ru.otus.hibernate.service;

public interface DBService<V> {

    void save(V value);

    V get(long id);

}

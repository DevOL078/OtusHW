package ru.otus.jdbc.service;

public interface DBService<T> {
    void createTable();

    void save(T objectDao);

    T get(long id);
}

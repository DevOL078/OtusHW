package ru.otus.cache;

public interface CacheEngine<K, V> {

    void put(K key, SmartValue<V> element);

    SmartValue<V> get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();

}

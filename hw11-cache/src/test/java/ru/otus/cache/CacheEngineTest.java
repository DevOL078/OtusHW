package ru.otus.cache;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheEngineTest {

    @Test
    void eternalCacheTest() {
        int size = 5;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 0, 0, true);

        for (int i = 0; i < 10; i++) {
            cache.put(i, "String " + i);
        }

        for (int i = 0; i < 10; i++) {
            Optional<String> optionalString = cache.get(i);
            System.out.println(i + " : " + (optionalString.orElse("null")));
        }

        assertEquals(5, cache.getHitCount());
        assertEquals(5, cache.getMissCount());

        cache.dispose();
    }

    @Test
    void lifeCacheTest() throws InterruptedException {
        int size = 5;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 1000, 0, false);

        for (int i = 0; i < size; i++) {
            cache.put(i, "String " + i);
        }

        for (int i = 0; i < size; i++) {
            Optional<String> optionalString = cache.get(i);
            System.out.println(i + " : " + (optionalString.orElse("null")));
        }

        assertEquals(size, cache.getHitCount());
        assertEquals(0, cache.getMissCount());

        System.out.println("--WAITING--");
        Thread.sleep(1000);

        for (int i = 0; i < size; i++) {
            Optional<String> optionalString = cache.get(i);
            System.out.println(i + " : " + (optionalString.orElse("null")));
        }

        assertEquals(size, cache.getHitCount());
        assertEquals(size, cache.getMissCount());

        cache.dispose();
    }

}
package ru.otus.hibernate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.hibernate.config.HibernateConfig;
import ru.otus.hibernate.dao.User;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserServiceWithCacheTest {

    private UserServiceWithCache userServiceWithCache;

    @BeforeEach
    public void beforeEach() {
        userServiceWithCache = new UserServiceWithCache(
                HibernateConfig.getSessionFactory(),
                new CacheEngineImpl<>(10, 1000, 0, false));
    }

    @Test
    public void testSaveAndGet() {
        for (int i = 0; i < 10; ++i) {
            User user = new User();
            user.setName("User " + i);
            user.setAge(i + 20);

            userServiceWithCache.save(user);
        }

        for (int i = 0; i < 10; ++i) {
            User user = userServiceWithCache.get(i + 1);
            assertNotNull(user);
            assertEquals("User " + i, user.getName());
            assertEquals(i + 20, user.getAge());
        }

        assertEquals(10, getHits());
        assertEquals(0, getMisses());
    }

    private CacheEngine getCacheEngine() {
        Field cacheEngineField = null;
        try {
            Class<? extends UserServiceWithCache> aClass = userServiceWithCache.getClass();
            cacheEngineField = aClass.getDeclaredField("cacheEngine");
            cacheEngineField.setAccessible(true);
            return (CacheEngine) cacheEngineField.get(userServiceWithCache);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            cacheEngineField.setAccessible(false);
        }
        return null;
    }

    private int getHits() {
        return getCacheEngine().getHitCount();
    }

    private int getMisses() {
        return getCacheEngine().getMissCount();
    }

}
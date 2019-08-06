package ru.otus.hibernate.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.SmartValue;
import ru.otus.hibernate.dao.User;

public class UserServiceWithCache {

    private final SessionFactory sessionFactory;
    private final CacheEngine<Long, User> cacheEngine;

    public UserServiceWithCache(SessionFactory sessionFactory, CacheEngine<Long, User> cacheEngine) {
        this.sessionFactory = sessionFactory;
        this.cacheEngine = cacheEngine;
    }

    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(user);

            session.getTransaction().commit();
        }

        cacheEngine.put(user.getId(), new SmartValue<>(user));
    }

    public User get(long id) {
        SmartValue<User> userSmartValue = cacheEngine.get(id);
        if(userSmartValue != null) {
            return userSmartValue.getValue();
        } else {
            try (Session session = sessionFactory.openSession()) {
                User userFromDB = session.get(User.class, id);
                if(userFromDB != null) {
                    cacheEngine.put(id, new SmartValue<>(userFromDB));
                }
                return userFromDB;
            }
        }
    }

}

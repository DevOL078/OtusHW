package ru.otus.hibernate.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.cache.CacheEngine;
import ru.otus.hibernate.dao.User;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class UserServiceWithCache implements DBService<User> {

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

        cacheEngine.put(user.getId(), user);
    }

    public User get(long id) {
        Optional<User> optionalUser = cacheEngine.get(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            try (Session session = sessionFactory.openSession()) {
                User userFromDB = session.get(User.class, id);
                if (userFromDB != null) {
                    cacheEngine.put(id, userFromDB);
                }
                return userFromDB;
            }
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            CriteriaQuery<User> all = criteriaQuery.select(root);

            TypedQuery<User> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        }
    }

}

package ru.otus.hibernate.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.dao.User;

public class UserService {

    private final SessionFactory sessionFactory;

    public UserService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(user);

            session.getTransaction().commit();
        }

    }

    public User get(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

}

package ru.otus.hibernate.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.dao.PhoneDataSet;

public class PhoneService implements DBService<PhoneDataSet> {

    private final SessionFactory sessionFactory;

    public PhoneService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(PhoneDataSet phone) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(phone);

            session.getTransaction().commit();
        }

    }

    public PhoneDataSet get(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(PhoneDataSet.class, id);
        }
    }

}

package ru.otus.hibernate.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.dao.AddressDataSet;

public class AddressService {

    private final SessionFactory sessionFactory;

    public AddressService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(AddressDataSet address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(address);

            session.getTransaction().commit();
        }

    }

    public AddressDataSet get(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(AddressDataSet.class, id);
        }
    }

}

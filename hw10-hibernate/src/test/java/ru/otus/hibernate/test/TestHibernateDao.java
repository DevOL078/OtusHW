package ru.otus.hibernate.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hibernate.dao.AddressDataSet;
import ru.otus.hibernate.dao.PhoneDataSet;
import ru.otus.hibernate.dao.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestHibernateDao {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void beforeAll() {
        Configuration conf = new Configuration()
                .configure("hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(conf.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    @Test
    void testAddress() {
        AddressDataSet createdAddress;
        AddressDataSet selectedAddress;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            createdAddress = new AddressDataSet();
            createdAddress.setStreet("MainStreet");

            session.save(createdAddress);
            System.out.println("created: " + createdAddress);

            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            selectedAddress = session.get(AddressDataSet.class, createdAddress.getId());
            System.out.println("selected: " + selectedAddress);
        }

        assertEquals(createdAddress, selectedAddress);
    }

    @Test
    void testPhone() {
        PhoneDataSet createdPhone;
        PhoneDataSet selectedPhone;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            createdPhone = new PhoneDataSet();
            createdPhone.setNumber("222-88-77");

            session.save(createdPhone);
            System.out.println("created: " + createdPhone);

            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            selectedPhone = session.get(PhoneDataSet.class, createdPhone.getId());
            System.out.println("selected: " + selectedPhone);
        }

        assertEquals(createdPhone, selectedPhone);
    }

    @Test
    void testUser() {
        User createdUser;
        User selectedUser;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            createdUser = new User();
            createdUser.setAge(22);
            createdUser.setName("Bond");

            AddressDataSet address = new AddressDataSet();
            address.setStreet("Main street");
            address.setUser(createdUser);

            PhoneDataSet phone1 = new PhoneDataSet();
            phone1.setNumber("111");
            phone1.setUser(createdUser);

            PhoneDataSet phone2 = new PhoneDataSet();
            phone2.setNumber("222");
            phone2.setUser(createdUser);

            createdUser.setAddress(address);
            List<PhoneDataSet> phones = new ArrayList<>();
            phones.add(phone1);
            phones.add(phone2);
            createdUser.setPhones(phones);
            session.save(createdUser);

            System.out.println("created: " + createdUser);
            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            selectedUser = session.get(User.class, createdUser.getId());
            System.out.println("selected: " + selectedUser);
        }

        assertEquals(createdUser, selectedUser);
        assertEquals(createdUser.getAddress(), selectedUser.getAddress());
        for (int i = 0; i < createdUser.getPhones().size(); ++i) {
            assertEquals(createdUser.getPhones().get(i),
                    selectedUser.getPhones().get(i));
        }
    }

}

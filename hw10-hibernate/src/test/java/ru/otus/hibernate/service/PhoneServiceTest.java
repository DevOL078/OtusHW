package ru.otus.hibernate.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hibernate.config.HibernateConfig;
import ru.otus.hibernate.dao.PhoneDataSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneServiceTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void beforeAll() {
        sessionFactory = HibernateConfig.getSessionFactory();
    }

    @Test
    public void testSaveAndGet() {
        PhoneService phoneService = new PhoneService(sessionFactory);
        PhoneDataSet newPhone = new PhoneDataSet();
        newPhone.setNumber("88002353535");

        phoneService.save(newPhone);
        assertTrue(newPhone.getId() > 0);

        PhoneDataSet selectedPhone = phoneService.get(newPhone.getId());
        assertEquals(newPhone, selectedPhone);
    }

}
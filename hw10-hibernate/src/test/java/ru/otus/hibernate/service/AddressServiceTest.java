package ru.otus.hibernate.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hibernate.config.HibernateConfig;
import ru.otus.hibernate.dao.AddressDataSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressServiceTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void beforeAll() {
        sessionFactory = HibernateConfig.getSessionFactory();
    }

    @Test
    public void testSaveAndGet() {
        AddressService addressService = new AddressService(sessionFactory);
        AddressDataSet newAddress = new AddressDataSet();
        newAddress.setStreet("Main street");

        addressService.save(newAddress);
        assertTrue(newAddress.getId() > 0);

        AddressDataSet selectedAddress = addressService.get(newAddress.getId());
        assertEquals(newAddress, selectedAddress);
    }

}
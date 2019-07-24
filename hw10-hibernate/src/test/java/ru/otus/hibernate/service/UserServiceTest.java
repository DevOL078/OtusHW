package ru.otus.hibernate.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hibernate.config.HibernateConfig;
import ru.otus.hibernate.dao.AddressDataSet;
import ru.otus.hibernate.dao.PhoneDataSet;
import ru.otus.hibernate.dao.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void beforeAll() {
        sessionFactory = HibernateConfig.getSessionFactory();
    }

    @Test
    public void testSaveAndGet() {
        UserService userService = new UserService(sessionFactory);

        User newUser = new User();
        newUser.setAge(22);
        newUser.setName("Bond");

        AddressDataSet address = new AddressDataSet();
        address.setStreet("Main street");
        address.setUser(newUser);

        PhoneDataSet phone1 = new PhoneDataSet();
        phone1.setNumber("111");
        phone1.setUser(newUser);

        PhoneDataSet phone2 = new PhoneDataSet();
        phone2.setNumber("222");
        phone2.setUser(newUser);

        newUser.setAddress(address);
        List<PhoneDataSet> phones = new ArrayList<>();
        phones.add(phone1);
        phones.add(phone2);
        newUser.setPhones(phones);

        userService.save(newUser);
        assertTrue(newUser.getId() > 0);

        User selectedUser = userService.get(newUser.getId());

        assertEquals(newUser, selectedUser);
        assertEquals(newUser.getAddress(), selectedUser.getAddress());
        for (int i = 0; i < newUser.getPhones().size(); ++i) {
            assertEquals(newUser.getPhones().get(i),
                    selectedUser.getPhones().get(i));
        }
    }

}
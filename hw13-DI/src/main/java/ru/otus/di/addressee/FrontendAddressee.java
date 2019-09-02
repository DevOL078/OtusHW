package ru.otus.di.addressee;

import ru.otus.hibernate.dao.User;
import ru.otus.ms.Addressee;

import java.util.List;

public interface FrontendAddressee extends Addressee {
    void getAll();

    void save(User user);

    void sendAllUsersToClient(List<User> users);
}

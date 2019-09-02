package ru.otus.di.addressee;

import ru.otus.hibernate.dao.User;
import ru.otus.ms.Addressee;

public interface DBAddressee extends Addressee {
    void getAll();

    void save(User user);
}

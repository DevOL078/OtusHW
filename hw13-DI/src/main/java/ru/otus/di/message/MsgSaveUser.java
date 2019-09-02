package ru.otus.di.message;

import ru.otus.di.addressee.DBAddressee;
import ru.otus.hibernate.dao.User;
import ru.otus.ms.Address;

public class MsgSaveUser extends MsgToDB {

    private final User user;

    public MsgSaveUser(Address from, Address to, User user) {
        super(from, to);
        this.user = user;
    }

    @Override
    public void exec(DBAddressee addressee) {
        addressee.save(user);
    }

}

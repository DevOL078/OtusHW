package ru.otus.di.message;

import ru.otus.di.addressee.FrontendAddressee;
import ru.otus.hibernate.dao.User;
import ru.otus.ms.Address;

import java.util.List;

public class MsgUsers extends MsgToFrontend {
    private final List<User> users;

    public MsgUsers(Address from, Address to, List<User> users) {
        super(from, to);
        this.users = users;
    }

    @Override
    public void exec(FrontendAddressee addressee) {
        addressee.sendAllUsersToClient(users);
    }
}

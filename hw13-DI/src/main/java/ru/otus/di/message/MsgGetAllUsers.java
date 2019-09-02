package ru.otus.di.message;

import ru.otus.di.addressee.DBAddressee;
import ru.otus.ms.Address;

public class MsgGetAllUsers extends MsgToDB {

    public MsgGetAllUsers(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(DBAddressee addressee) {
        addressee.getAll();
    }
}

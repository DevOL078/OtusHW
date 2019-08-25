package ru.otus.di.message;

import ru.otus.di.addressee.DBAddressee;
import ru.otus.ms.Address;
import ru.otus.ms.Addressee;
import ru.otus.ms.Message;

public abstract class MsgToDB extends Message {

    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    public void exec(Addressee addressee) {
        if (addressee instanceof DBAddressee) {
            exec((DBAddressee) addressee);
        }
    }

    public abstract void exec(DBAddressee addressee);

}

package ru.otus.di.message;

import ru.otus.di.addressee.FrontendAddressee;
import ru.otus.ms.Address;
import ru.otus.ms.Addressee;
import ru.otus.ms.Message;

public abstract class MsgToFrontend extends Message {

    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendAddressee) {
            exec((FrontendAddressee) addressee);
        }
    }

    public abstract void exec(FrontendAddressee addressee);
}

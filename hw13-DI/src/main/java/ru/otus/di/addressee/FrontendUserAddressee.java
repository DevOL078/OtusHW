package ru.otus.di.addressee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.di.message.MsgGetAllUsers;
import ru.otus.di.message.MsgSaveUser;
import ru.otus.di.utils.MessageSystemContext;
import ru.otus.di.ws.WebSocketSender;
import ru.otus.hibernate.dao.User;
import ru.otus.ms.Address;
import ru.otus.ms.Message;
import ru.otus.ms.MessageSystem;

import java.util.List;

public class FrontendUserAddressee implements FrontendAddressee {
    private final Address address;
    private final MessageSystemContext context;
    private final WebSocketSender webSocketSender;

    private Logger logger = LoggerFactory.getLogger("AppLogger");

    public FrontendUserAddressee(MessageSystemContext context, Address address, WebSocketSender webSocketSender) {
        this.context = context;
        this.address = address;
        this.webSocketSender = webSocketSender;
    }

    @Override
    public void init() {
        getMS().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    @Override
    public void getAll() {
        logger.info("create message to DB to get all users");
        Message message = new MsgGetAllUsers(
                context.getFrontAddress(),
                context.getDbAddress());
        getMS().sendMessage(message);
    }

    @Override
    public void save(User user) {
        logger.info("create message to DB to save user");
        System.out.println(user);
        Message message = new MsgSaveUser(
                context.getFrontAddress(),
                context.getDbAddress(),
                user);
        getMS().sendMessage(message);
    }

    @Override
    public void sendAllUsersToClient(List<User> users) {
        logger.info("call websocket sender");
        System.out.println(users);
        webSocketSender.sendUsersToClient(users);
    }

}

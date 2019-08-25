package ru.otus.di.addressee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.di.message.MsgUsers;
import ru.otus.di.utils.MessageSystemContext;
import ru.otus.hibernate.dao.User;
import ru.otus.hibernate.service.DBService;
import ru.otus.ms.Address;
import ru.otus.ms.Message;
import ru.otus.ms.MessageSystem;

import java.util.List;

public class DBUserAddressee implements DBAddressee {
    private final Address address;
    private final MessageSystemContext context;

    private Logger logger = LoggerFactory.getLogger("AppLogger");

    @Autowired
    private DBService<User> userDBService;

    public DBUserAddressee(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
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
        logger.info("request to get all users");
        List<User> users = userDBService.getAll();
        logger.info("create message to Frontend with users");
        System.out.println(users);
        Message message = new MsgUsers(
                context.getDbAddress(),
                context.getFrontAddress(),
                users);
        getMS().sendMessage(message);
    }

    @Override
    public void save(User user) {
        logger.info("request to save user");
        System.out.println(user);
        userDBService.save(user);
        logger.info("user has been sent");
    }
}

package ru.otus.di.controller;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.otus.di.addressee.FrontendAddressee;
import ru.otus.di.utils.AddressDataSetTypeAdapter;
import ru.otus.di.utils.UserSerializationExclusionStrategy;
import ru.otus.hibernate.dao.AddressDataSet;
import ru.otus.hibernate.dao.User;

@Controller
public class UserController {

    private GsonBuilder gsonBuilder;

    @Autowired
    private FrontendAddressee frontendAddressee;

    private Logger logger = LoggerFactory.getLogger("AppLogger");

    public UserController() {
        this.gsonBuilder = new GsonBuilder()
                .addSerializationExclusionStrategy(new UserSerializationExclusionStrategy())
                .registerTypeAdapter(AddressDataSet.class, new AddressDataSetTypeAdapter());
    }

    @MessageMapping("/save")
    public void save(String message) {
        logger.info("receive new message");
        System.out.println(message);
        User user = gsonBuilder.create().fromJson(message, User.class);
        if (user.getAddress() != null) {
            user.getAddress().setUser(user);
        }
        frontendAddressee.save(user);
    }

    @MessageMapping("/getAll")
    public void getAll() {
        logger.info("get all users");
        frontendAddressee.getAll();
    }

}

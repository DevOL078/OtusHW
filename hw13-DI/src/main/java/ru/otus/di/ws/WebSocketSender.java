package ru.otus.di.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.otus.di.utils.AddressDataSetTypeAdapter;
import ru.otus.di.utils.UserSerializationExclusionStrategy;
import ru.otus.hibernate.dao.AddressDataSet;
import ru.otus.hibernate.dao.User;

import java.util.List;

public class WebSocketSender {

    private GsonBuilder gsonBuilder;
    private SimpMessagingTemplate template;

    private Logger logger = LoggerFactory.getLogger("AppLogger");


    public WebSocketSender(SimpMessagingTemplate template) {
        this.template = template;
        this.gsonBuilder = new GsonBuilder()
                .addSerializationExclusionStrategy(new UserSerializationExclusionStrategy())
                .registerTypeAdapter(AddressDataSet.class, new AddressDataSetTypeAdapter());
    }

    public void sendUsersToClient(List<User> users) {
        logger.info("send all users to client via websocket");
        Gson gson = gsonBuilder.create();
        template.convertAndSend("/topic/users", gson.toJson(users));
    }

}

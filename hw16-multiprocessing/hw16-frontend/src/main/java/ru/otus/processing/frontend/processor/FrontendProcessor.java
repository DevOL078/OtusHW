package ru.otus.processing.frontend.processor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.ms.dto.UserDto;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FrontendProcessor {

    private static final Logger logger = LoggerFactory.getLogger("Frontend-Logger");
    private static final String FRONTEND_ADDRESS = "Frontend";
    private static final String DB_ADDRESS = "DB";

    private final SocketMessageWorker worker;

    public FrontendProcessor(SocketMessageWorker worker) {
        this.worker = worker;
    }

    public List<UserDto> getAll() throws InterruptedException {
        Message sendingMsg = new Message(
                FRONTEND_ADDRESS,
                DB_ADDRESS,
                "getAll",
                null
        );
        worker.send(sendingMsg);
        logger.info("New message to get all users has been saved");
        logger.info("Wait for message with all users");
        Message receivedMsg = worker.take();
        logger.info("New message with users: " + receivedMsg);
        if(!receivedMsg.getType().equals("allUsers")) {
            throw new IllegalStateException("Message type is illegal");
        }
        return getUsersDtoFromMessage(receivedMsg);
    }

    private List<UserDto> getUsersDtoFromMessage(Message message) {
        String json = message.getMessage();
        logger.info("Receive: " + json);
        Gson gson = new Gson();
        Type listUserDto = new TypeToken<List<UserDto>>() {}.getType();
        return gson.fromJson(json, listUserDto);
    }

    public void save(UserDto userDto) {
        Gson gson = new Gson();
        Message message = new Message(
                FRONTEND_ADDRESS,
                DB_ADDRESS,
                "save",
                gson.toJson(userDto)
        );
        worker.send(message);
        logger.info("New message to save user has been saved: " + message);
    }

}

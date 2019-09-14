package ru.otus.processing.frontend.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.processing.frontend.processor.FrontendProcessor;
import ru.otus.processing.frontend.websocket.WebSocketSender;
import ru.otus.processing.ms.dto.UserDto;

import java.util.List;

@RestController("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final FrontendProcessor frontendProcessor;
    private final WebSocketSender webSocketSender;

    public UserController(FrontendProcessor frontendProcessor,
                          WebSocketSender webSocketSender) {
        this.frontendProcessor = frontendProcessor;
        this.webSocketSender = webSocketSender;
    }

    @MessageMapping("/save")
    public void save(String message) {
        logger.info("Call save");
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String address = jsonObject.get("address").getAsString();
        int age = jsonObject.get("age").getAsInt();
        UserDto userDto = new UserDto(name, address, age);
        frontendProcessor.save(userDto);
    }

    @MessageMapping("/getAll")
    public void getAll() throws InterruptedException {
        logger.info("Call getAll");
        List<UserDto> usersDto = frontendProcessor.getAll();
        webSocketSender.sendUsersToClient(usersDto);
    }

}

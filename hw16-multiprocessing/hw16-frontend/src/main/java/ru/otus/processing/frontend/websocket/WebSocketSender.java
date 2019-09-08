package ru.otus.processing.frontend.websocket;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.otus.processing.ms.dto.UserDto;

import java.util.List;

public class WebSocketSender {
    private static final Logger logger = LoggerFactory.getLogger("Frontend-Logger");

    private final SimpMessagingTemplate template;

    public WebSocketSender(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendUsersToClient(List<UserDto> users) {
        logger.info("Send all users to client via websocket: " + users);
        Gson gson = new Gson();
        template.convertAndSend("/topic/users", gson.toJson(users));
    }

}

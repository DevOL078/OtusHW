package ru.otus.processing.frontend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @MessageMapping("/save")
    public void save(String message) {
        logger.info("Call save");
    }

    @MessageMapping("/getAll")
    public void getAll() {
        logger.info("Call getAll");
    }

}

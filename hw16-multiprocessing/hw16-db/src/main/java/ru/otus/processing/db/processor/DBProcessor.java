package ru.otus.processing.db.processor;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.dao.AddressDataSet;
import ru.otus.hibernate.dao.User;
import ru.otus.hibernate.service.DBService;
import ru.otus.processing.db.config.DBConfigManager;
import ru.otus.processing.ms.dto.UserDto;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DBProcessor {
    private final Logger logger = LoggerFactory.getLogger(DBProcessor.class);

    private final String dbAddress;

    private final DBService<User> dbService;
    private final SocketMessageWorker worker;
    private final ExecutorService executorService;


    public DBProcessor(DBService<User> dbService, SocketMessageWorker worker) {
        this.dbAddress = DBConfigManager.getInstance().getServiceId();
        this.dbService = dbService;
        this.worker = worker;
        int threadCount = DBConfigManager.getInstance().getIntConfig("db.processor.thread-count");
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void init() {
        this.executorService.submit(() -> {
            while (!executorService.isShutdown()) {
                receiveMessage();
            }
        });
    }

    private void receiveMessage() {
        try {
            Message message = worker.take();
            logger.info("New message: " + message);
            process(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void process(Message message) {
        String type = message.getType();
        switch (type) {
            case "getAll": {
                getAllUsers(message);
                break;
            }
            case "save": {
                saveUser(message);
                break;
            }
            default: {
                throw new IllegalStateException("Illegal type of message");
            }
        }
    }

    private User getUserFrom(String message) {
        Gson gson = new Gson();
        UserDto userDto = gson.fromJson(message, UserDto.class);
        logger.info("UserDto: " + userDto);
        User user = new User(userDto.getName(), userDto.getAge());
        AddressDataSet addressDataSet = new AddressDataSet(userDto.getAddress());
        user.setAddress(addressDataSet);
        addressDataSet.setUser(user);
        return user;
    }

    private void getAllUsers(Message message) {
        List<User> users = dbService.getAll();
        logger.info("Users from database: " + users);
        List<UserDto> usersDto = users.stream().map(u -> new UserDto(
                u.getName(),
                u.getAddress().getStreet(),
                u.getAge()
        )).collect(Collectors.toList());
        Gson gson = new Gson();
        Message sendingMessage = new Message(
                dbAddress,
                message.getFrom(),
                "allUsers",
                gson.toJson(usersDto)
        );
        worker.send(sendingMessage);
        logger.info(String.format("New message from %s to %s has been sent", sendingMessage.getFrom(), sendingMessage.getTo()));
    }

    private void saveUser(Message message) {
        User user = getUserFrom(message.getMessage());
        dbService.save(user);
        logger.info("New user saved: " + user);
    }

}

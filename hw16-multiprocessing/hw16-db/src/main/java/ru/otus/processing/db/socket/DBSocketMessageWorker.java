package ru.otus.processing.db.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class DBSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger("DB-Logger");

    private static final String MS_ADDRESS = "MS";
    private static final String DB_ADDRESS = "DB";

    public DBSocketMessageWorker(Socket socket) {
        super(socket);
        logger.info("Socket has been connected");
    }

    @Override
    public void init() {
        super.init();

        super.send(new Message(
                DB_ADDRESS,
                MS_ADDRESS,
                "service-id",
                null
        ));
        logger.info("ServiceId has been sent");
    }
}

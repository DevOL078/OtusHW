package ru.otus.processing.db.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class DBSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger(DBSocketMessageWorker.class);

    private static final String MS_ID = "MS";
    private static final String SERVICE_ID = "DB";

    public DBSocketMessageWorker(Socket socket) throws IOException {
        super(socket);
        logger.info("Socket has been connected");
    }

    @Override
    public void init() {
        super.init();

        super.send(new Message(
                SERVICE_ID,
                MS_ID,
                "service-id",
                null
        ));
        logger.info("ServiceId has been sent");
    }
}

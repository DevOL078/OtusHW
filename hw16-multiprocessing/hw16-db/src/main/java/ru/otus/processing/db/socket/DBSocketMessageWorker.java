package ru.otus.processing.db.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.db.config.DBConfigManager;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.net.Socket;

public class DBSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger(DBSocketMessageWorker.class);

    private final String msAddress = DBConfigManager.getInstance().getStringConfig("ms.address");
    private final String dbAddress = DBConfigManager.getInstance().getStringConfig("db.address");

    public DBSocketMessageWorker(Socket socket) {
        super(socket);
        logger.info("Socket has been connected");
    }

    @Override
    public void init() {
        super.init();

        super.send(new Message(
                dbAddress,
                msAddress,
                "service-id",
                null
        ));
        logger.info("ServiceId has been sent");
    }
}

package ru.otus.processing.db.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.db.config.DBConfigManager;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.net.Socket;

public class DBSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger(DBSocketMessageWorker.class);

    private final String msAddress;
    private final String dbAddress;

    public DBSocketMessageWorker(Socket socket) {
        super(socket);
        logger.info("Socket has been connected");

        this.msAddress = DBConfigManager.getInstance().getStringConfig("ms.address");
        this.dbAddress = DBConfigManager.getInstance().getServiceId();
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

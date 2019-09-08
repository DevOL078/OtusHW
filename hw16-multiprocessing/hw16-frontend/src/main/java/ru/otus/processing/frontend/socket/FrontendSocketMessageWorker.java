package ru.otus.processing.frontend.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class FrontendSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger("Frontend-Logger");

    private static final String MS_ADDRESS = "MS";       //TODO придумать, как вынести в конфиг
    private static final String FRONTEND_ADDRESS = "Frontend";

    public FrontendSocketMessageWorker(Socket socket) {
        super(socket);
        logger.info("Socket has been connected");
    }

    @Override
    public void init() {
        super.init();

        super.send(new Message(
                FRONTEND_ADDRESS,
                MS_ADDRESS,
                "service-id",
                null
        ));
        logger.info("ServiceId has been sent");
    }

}

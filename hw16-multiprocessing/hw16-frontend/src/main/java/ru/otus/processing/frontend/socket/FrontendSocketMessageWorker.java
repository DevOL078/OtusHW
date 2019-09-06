package ru.otus.processing.frontend.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class FrontendSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger(FrontendSocketMessageWorker.class);

    private static final String MS_ID = "MS";       //TODO придумать, как вынести в конфиг
    private static final String SERVICE_ID = "Frontend";

    public FrontendSocketMessageWorker(Socket socket) throws IOException {
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

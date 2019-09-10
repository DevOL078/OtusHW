package ru.otus.processing.frontend.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.frontend.config.FrontendConfigManager;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.net.Socket;

public class FrontendSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger(FrontendSocketMessageWorker.class);

    private final String msAddress = FrontendConfigManager.getInstance().getStringConfig("ms.address");
    private final String frontendAddress = FrontendConfigManager.getInstance().getStringConfig("frontend.address");

    public FrontendSocketMessageWorker(Socket socket) {
        super(socket);
        logger.info("Socket has been connected");
    }

    @Override
    public void init() {
        super.init();

        super.send(new Message(
                frontendAddress,
                msAddress,
                "service-id",
                null
        ));
        logger.info("ServiceId has been sent: " + frontendAddress);
    }

}

package ru.otus.processing.frontend.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.frontend.config.FrontendConfigManager;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class FrontendSocketMessageWorker extends SocketMessageWorker {
    private final Logger logger = LoggerFactory.getLogger(FrontendSocketMessageWorker.class);

    private final String msAddress;
    private final String frontendAddress;

    public FrontendSocketMessageWorker(FrontendConfigManager frontendConfigManager) throws IOException {
        super(new Socket(
                frontendConfigManager.getStringConfig("ms.host"),
                frontendConfigManager.getIntConfig("ms.port")
        ));
        logger.info("Socket has been connected");
        this.msAddress = frontendConfigManager.getStringConfig("ms.address");
        this.frontendAddress = frontendConfigManager.getServiceId();
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

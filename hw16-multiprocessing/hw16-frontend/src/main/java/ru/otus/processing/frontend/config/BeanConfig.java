package ru.otus.processing.frontend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.otus.processing.frontend.socket.FrontendSocketMessageWorker;
import ru.otus.processing.frontend.websocket.WebSocketSender;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;

@Configuration
public class BeanConfig {

    private final FrontendConfigManager frontendConfigManager;

    private final String msHost;
    private final int msPort;

    public BeanConfig(FrontendConfigManager frontendConfigManager) {
        this.frontendConfigManager = frontendConfigManager;
        this.msHost = frontendConfigManager.getStringConfig("ms.host");
        this.msPort = frontendConfigManager.getIntConfig("ms.port");
    }

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public SocketMessageWorker frontendSocketMessageWorker() throws IOException {
        SocketMessageWorker socketMessageWorker = new FrontendSocketMessageWorker(frontendConfigManager);
        socketMessageWorker.init();
        return socketMessageWorker;
    }

    @Bean
    public WebSocketSender webSocketSender() {
        return new WebSocketSender(simpMessagingTemplate);
    }

}

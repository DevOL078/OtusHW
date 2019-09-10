package ru.otus.processing.frontend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.otus.processing.frontend.processor.FrontendProcessor;
import ru.otus.processing.frontend.socket.FrontendSocketMessageWorker;
import ru.otus.processing.frontend.websocket.WebSocketSender;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

@Configuration
public class BeanConfig {

    private final String msHost = FrontendConfigManager.getInstance().getStringConfig("ms.host");
    private final int msPort = FrontendConfigManager.getInstance().getIntConfig("ms.port");

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public SocketMessageWorker frontendSocketMessageWorker() throws IOException {
        SocketMessageWorker socketMessageWorker = new FrontendSocketMessageWorker(new Socket(msHost, msPort));
        socketMessageWorker.init();
        return socketMessageWorker;
    }

    @Bean
    public FrontendProcessor frontendProcessor() throws IOException {
        return new FrontendProcessor(frontendSocketMessageWorker());
    }

    @Bean
    public WebSocketSender webSocketSender() {
        return new WebSocketSender(simpMessagingTemplate);
    }

}

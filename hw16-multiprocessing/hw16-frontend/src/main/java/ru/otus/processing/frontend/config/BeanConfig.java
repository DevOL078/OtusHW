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

    private static final String MS_HOST = "localhost"; //TODO придумать, как вынести в конфиг
    private static final int MS_PORT = 8080;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public SocketMessageWorker frontendSocketMessageWorker() throws IOException {
        SocketMessageWorker socketMessageWorker = new FrontendSocketMessageWorker(new Socket(MS_HOST, MS_PORT));
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

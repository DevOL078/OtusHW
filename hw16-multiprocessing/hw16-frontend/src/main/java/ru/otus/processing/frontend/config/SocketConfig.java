package ru.otus.processing.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.processing.frontend.socket.FrontendSocketMessageWorker;
import ru.otus.processing.ms.socket.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

@Configuration
public class SocketConfig {

    private static final String MS_HOST = "localhost"; //TODO придумать, как вынести в конфиг
    private static final int MS_PORT = 8080;

    @Bean
    public SocketMessageWorker frontendSocketMessageWorker() throws IOException {
        SocketMessageWorker socketMessageWorker = new FrontendSocketMessageWorker(new Socket(MS_HOST, MS_PORT));
        socketMessageWorker.init();
        return socketMessageWorker;
    }

}

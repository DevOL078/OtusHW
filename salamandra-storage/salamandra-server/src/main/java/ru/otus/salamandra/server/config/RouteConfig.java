package ru.otus.salamandra.server.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.salamandra.server.processor.FileProcessor;

@Component
public class RouteConfig extends RouteBuilder {

    @Value("${application.rabbit.updateExchange}")
    private String updateExchange;

    @Override
    public void configure() {
        from("rabbitmq://localhost/" + updateExchange)
                .log("body")
                .bean(FileProcessor.class)
                .end();
    }

}

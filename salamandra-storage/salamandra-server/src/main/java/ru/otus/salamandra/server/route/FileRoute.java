package ru.otus.salamandra.server.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import ru.otus.salamandra.server.processor.FileProcessor;

@Component
public class FileRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("rabbitmq://localhost/sync")
                .log("body")
                .bean(FileProcessor.class)
                .end();
    }

}

package ru.otus.salamandra.app.route;

import org.apache.camel.builder.RouteBuilder;
import ru.otus.salamandra.app.sync.SyncProcessor;

public class SyncRoute extends RouteBuilder {

    private final String dirPath = "C:\\Users\\Dmitry\\Desktop\\Otus\\OtusHW\\salamandra-storage\\salamandra-app\\files";

    @Override
    public void configure() {
        from("file://" + dirPath + "?noop=true&charset=utf-8&recursive=true")
                .bean(SyncProcessor.class)
                .log("body")
                .to("rabbitmq://localhost/sync")
                .end();
    }

}

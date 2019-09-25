package ru.otus.salamandra.app.config;

import org.apache.camel.builder.RouteBuilder;
import ru.otus.salamandra.app.update.UpdateProcessor;

public class RouteConfig extends RouteBuilder {

    private final String dirPath = "C:\\Users\\Dmitry\\Desktop\\Otus\\OtusHW\\salamandra-storage\\salamandra-app\\files";
    private final int DELAY_PERIOD = 5;


    @Override
    public void configure() {
        from("file://" + dirPath + "?noop=true&charset=utf-8&recursive=true&" +
                "idempotent=false&delay=" + DELAY_PERIOD + "s")
                .bean(UpdateProcessor.class)
                .log("body")
                .choice()
                    .when(body().isNull()).stop()
                    .otherwise().to("rabbitmq://localhost/update")
                .endChoice();

        from("rabbitmq://localhost/sync?routingKey=test")
                .log("Receive new files")
                .log("body");
    }

}

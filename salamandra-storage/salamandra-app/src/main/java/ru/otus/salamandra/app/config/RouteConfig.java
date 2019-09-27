package ru.otus.salamandra.app.config;

import org.apache.camel.builder.RouteBuilder;
import ru.otus.salamandra.app.store.SessionStore;
import ru.otus.salamandra.app.sync.SyncProcessor;
import ru.otus.salamandra.app.update.UpdateProcessor;

public class RouteConfig extends RouteBuilder {

    private final String dirPath = SessionStore.getInstance().getBaseDirPath();
    private final int delaySec = AppConfigManager.getInstance().getIntConf("update.delaySec");


    @Override
    public void configure() {
        from("file://" + dirPath + "?noop=true&charset=utf-8&recursive=true&" +
                "idempotent=false&delay=" + delaySec + "s")
                .bean(UpdateProcessor.class)
                .log("body")
                .choice()
                    .when(body().isNull()).stop()
                    .otherwise().to("rabbitmq://localhost/update")
                .endChoice();

        String routingKey = SessionStore.getInstance().getUserLogin() + "/" +
                SessionStore.getInstance().getAppId();
        from("rabbitmq://localhost/sync?routingKey=" + routingKey)
                .log("Receive new files")
                .log("body")
                .bean(SyncProcessor.class, "perform(body)");
    }

}

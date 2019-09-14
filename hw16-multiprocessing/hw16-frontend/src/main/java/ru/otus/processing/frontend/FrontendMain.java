package ru.otus.processing.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Optional;

@SpringBootApplication
@ComponentScan({
        "ru.otus.processing.frontend"
})
public class FrontendMain {

    private static final Logger logger = LoggerFactory.getLogger(FrontendMain.class);

    private static Optional<String> serviceId;

    public static void main(String[] args) {
        logger.info("Start Frontend service");

        if (args.length > 0) {
            serviceId = Optional.of(args[0]);
        } else {
            serviceId = Optional.empty();
        }

        SpringApplication.run(FrontendMain.class, args);
    }

    public static Optional<String> getServiceId() {
        return serviceId;
    }

}

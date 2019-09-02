package ru.otus.processing.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendMain {

    private static final Logger logger = LoggerFactory.getLogger(FrontendMain.class);

    public static void main(String[] args) {
        logger.info("Hello from Frontend");

        SpringApplication.run(FrontendMain.class, args);
    }

}

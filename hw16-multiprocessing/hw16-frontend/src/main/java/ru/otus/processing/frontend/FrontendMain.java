package ru.otus.processing.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

@SpringBootApplication
@EnableWebMvc
@ComponentScan({
        "ru.otus.processing.frontend"
})
public class FrontendMain {

    private static final Logger logger = LoggerFactory.getLogger(FrontendMain.class);

    private static Optional<String> configFileName;

    public static void main(String[] args) {
        logger.info("Start Frontend service");

        if(args.length > 0) {
            configFileName = Optional.of(args[0]);
        } else {
            configFileName = Optional.empty();
        }

        SpringApplication.run(FrontendMain.class, args);
    }

    public static Optional<String> getConfigFileName() {
        return configFileName;
    }

}

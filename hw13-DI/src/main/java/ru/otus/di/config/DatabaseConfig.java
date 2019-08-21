package ru.otus.di.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.hibernate.config.HibernateConfig;
import ru.otus.hibernate.dao.User;
import ru.otus.hibernate.service.DBService;
import ru.otus.hibernate.service.UserServiceWithCache;

@Configuration
@PropertySource("application.properties")
public class DatabaseConfig {

    @Value("${application.cache.maxElements}")
    private final int maxElements = 10;

    @Value("${application.cache.lifeTimeMs}")
    private final int lifeTimeMs = 100;

    @Value("${application.cache.idleTimeMs}")
    private final int idleTimeMs = 100;

    @Value("${application.cache.isEternal}")
    private final boolean isEternal = false;

    @Bean
    public DBService<User> userDBService() {
        return new UserServiceWithCache(
                HibernateConfig.getSessionFactory(),
                new CacheEngineImpl<>(
                        maxElements,
                        lifeTimeMs,
                        idleTimeMs,
                        isEternal
                )
        );
    }

}

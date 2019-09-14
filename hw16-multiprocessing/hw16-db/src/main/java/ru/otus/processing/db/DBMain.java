package ru.otus.processing.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.hibernate.dao.User;
import ru.otus.hibernate.service.DBService;
import ru.otus.hibernate.service.UserServiceWithCache;
import ru.otus.processing.db.config.DBConfigManager;
import ru.otus.processing.db.config.HibernateConfig;
import ru.otus.processing.db.processor.DBProcessor;
import ru.otus.processing.db.socket.DBSocketMessageWorker;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class DBMain {

    private static Logger logger = LoggerFactory.getLogger(DBMain.class);

    private static Optional<String> serviceId;

    public static void main(String[] args) throws IOException {
        logger.info("Start DB service");

        if(args.length > 0) {
            serviceId = Optional.of(args[0]);
        } else {
            serviceId = Optional.empty();
        }

        String msHost = DBConfigManager.getInstance().getStringConfig("ms.host");
        int msPort = DBConfigManager.getInstance().getIntConfig("ms.port");
        DBSocketMessageWorker dbSocketMessageWorker = new DBSocketMessageWorker(new Socket(msHost, msPort));
        dbSocketMessageWorker.init();

        int cacheMaxElements = DBConfigManager.getInstance().getIntConfig("db.cache.maxElements");
        int cacheLifeTimeMs = DBConfigManager.getInstance().getIntConfig("db.cache.lifeTimeMs");
        int cacheIdleTimeMs = DBConfigManager.getInstance().getIntConfig("db.cache.idleTimeMs");
        boolean cacheIsEternal = DBConfigManager.getInstance().getBooleanConfig("db.cache.isEternal");

        DBService<User> dbService = new UserServiceWithCache(
                HibernateConfig.getSessionFactory(),
                new CacheEngineImpl<>(cacheMaxElements, cacheLifeTimeMs, cacheIdleTimeMs, cacheIsEternal)
        );

        DBProcessor dbProcessor = new DBProcessor(dbService, dbSocketMessageWorker);
        dbProcessor.init();
    }

    public static Optional<String> getServiceId() {
        return serviceId;
    }
}

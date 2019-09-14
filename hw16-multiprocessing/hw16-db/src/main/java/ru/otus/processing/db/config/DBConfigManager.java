package ru.otus.processing.db.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ru.otus.processing.db.DBMain;

import java.util.UUID;

public class DBConfigManager {
    private static DBConfigManager instance = new DBConfigManager();
    private final String serviceId;
    private final String CONFIG_FILE_NAME = "database.conf";
    private final Config config;

    private DBConfigManager() {
        serviceId = DBMain.getServiceId().orElse(UUID.randomUUID().toString());
        config = ConfigFactory.load(CONFIG_FILE_NAME);
    }

    public static DBConfigManager getInstance() {
        return instance;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getStringConfig(String configName) {
        return config.getString(configName);
    }

    public int getIntConfig(String configName) {
        return config.getInt(configName);
    }

    public boolean getBooleanConfig(String configName) {
        return config.getBoolean(configName);
    }

}

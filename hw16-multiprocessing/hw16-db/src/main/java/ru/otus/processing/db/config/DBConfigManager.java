package ru.otus.processing.db.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ru.otus.processing.db.DBMain;

public class DBConfigManager {
    private static volatile DBConfigManager instance;

    private Config config;

    private DBConfigManager() {
        String configFileName = DBMain.getConfigFileName().orElse("database1.conf");
        config = ConfigFactory.load(configFileName);
    }

    public static DBConfigManager getInstance() {
        if(instance == null) {
            synchronized (DBConfigManager.class) {
                instance = new DBConfigManager();
            }
        }
        return instance;
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

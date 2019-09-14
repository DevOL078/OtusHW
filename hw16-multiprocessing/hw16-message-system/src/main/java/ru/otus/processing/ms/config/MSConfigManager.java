package ru.otus.processing.ms.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MSConfigManager {
    private static MSConfigManager instance = new MSConfigManager();
    private final String CONFIG_FILE_NAME = "message-system.conf";
    private final Config config;

    private MSConfigManager(){
        this.config = ConfigFactory.load(CONFIG_FILE_NAME);
    }

    public static MSConfigManager getInstance() {
        return instance;
    }

    public String getStringConfig(String configName) {
        return config.getString(configName);
    }

    public int getIntConfig(String configName) {
        return config.getInt(configName);
    }

}

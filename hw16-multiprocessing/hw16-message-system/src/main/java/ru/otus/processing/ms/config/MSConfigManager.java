package ru.otus.processing.ms.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MSConfigManager {
    private static volatile MSConfigManager instance;

    private final String CONFIG_FILE_NAME = "message-system.conf";
    private final Config config;

    private MSConfigManager(){
        this.config = ConfigFactory.load(CONFIG_FILE_NAME);
    }

    public static MSConfigManager getInstance() {
        if(instance == null) {
            synchronized (MSConfigManager.class) {
                instance = new MSConfigManager();
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

}

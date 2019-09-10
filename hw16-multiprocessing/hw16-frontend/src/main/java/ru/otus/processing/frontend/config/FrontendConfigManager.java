package ru.otus.processing.frontend.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ru.otus.processing.frontend.FrontendMain;

public class FrontendConfigManager {
    private static volatile FrontendConfigManager instance;

    private Config config;

    private FrontendConfigManager() {
        String configFileName = FrontendMain.getConfigFileName().orElse("frontend1.conf");
        config = ConfigFactory.load(configFileName);
    }

    public static FrontendConfigManager getInstance() {
        if(instance == null) {
            synchronized (FrontendConfigManager.class) {
                instance = new FrontendConfigManager();
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

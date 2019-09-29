package ru.otus.salamandra.app.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ru.otus.salamandra.app.AppMain;

public class AppConfigManager {

    private static AppConfigManager instance = new AppConfigManager();
    private final String DEFAULT_CONF_NAME = "client-default.conf";
    private Config config;

    private AppConfigManager() {
        config = ConfigFactory.load(AppMain.getConfigName().orElse(DEFAULT_CONF_NAME));
    }

    public static AppConfigManager getInstance() {
        return instance;
    }

    public String getStringConf(String confName) {
        return config.getString(confName);
    }

    public int getIntConf(String confName) {
        return config.getInt(confName);
    }

}

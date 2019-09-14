package ru.otus.processing.frontend.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.stereotype.Service;
import ru.otus.processing.frontend.FrontendMain;

import java.util.UUID;

@Service
public class FrontendConfigManager {

    private Config config;
    private final String CONFIG_FILE_NAME = "frontend.conf";
    private final String serviceId;

    public FrontendConfigManager() {
        serviceId = FrontendMain.getServiceId().orElse(UUID.randomUUID().toString());
        config = ConfigFactory.load(CONFIG_FILE_NAME);
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

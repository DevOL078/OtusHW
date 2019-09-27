package ru.otus.salamandra.app.update;

import com.google.gson.Gson;
import org.apache.camel.Exchange;
import ru.otus.salamandra.app.config.AppConfigManager;
import ru.otus.salamandra.app.store.FileNameStorage;
import ru.otus.salamandra.app.store.SessionStore;
import ru.otus.salamandra.dto.FileDto;
import ru.otus.salamandra.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class UpdateProcessor {

    private final String baseDirPath = SessionStore.getInstance().getBaseDirPath();
    private final int delayPeriod = AppConfigManager.getInstance().getIntConf("update.delaySec");

    public String process(Exchange exchange) {
        File fileInput = exchange.getIn().getBody(File.class);
        if(checkFileName(fileInput.getAbsolutePath())) {
            return null;
        }
        System.out.println("Update file: " + fileInput.getAbsolutePath());
        List<String> relativePath = FileUtil.getRelativePath(baseDirPath, fileInput.getAbsolutePath());
        byte[] content = readAllBytes(fileInput);
        int fileSize = content.length;
        LocalDateTime lastModifiedTime = FileUtil.getLastModifiedTime(fileInput.getAbsolutePath());
        String login = SessionStore.getInstance().getUserLogin();

        return new Gson().toJson(
                new FileDto(
                        relativePath,
                        fileSize,
                        content,
                        lastModifiedTime,
                        login)
        );
    }

    private byte[] readAllBytes(File file) {
        try (FileInputStream fs = new FileInputStream(file)) {
            return fs.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkFileName(String fileName) {
        return FileNameStorage.getInstance().checkAndStore(fileName) &&
                !FileUtil.getLastModifiedTime(fileName)
                        .isAfter(LocalDateTime.now().minusSeconds(delayPeriod));
    }

}

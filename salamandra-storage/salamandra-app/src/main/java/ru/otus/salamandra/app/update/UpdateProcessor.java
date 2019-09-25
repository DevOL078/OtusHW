package ru.otus.salamandra.app.update;

import com.google.gson.Gson;
import org.apache.camel.Exchange;
import ru.otus.salamandra.app.store.FileNameStorage;
import ru.otus.salamandra.dto.FileDto;
import ru.otus.salamandra.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateProcessor {

    private final String dirPath = "C:\\Users\\Dmitry\\Desktop\\Otus\\OtusHW\\salamandra-storage\\salamandra-app\\files";
    private final int DELAY_PERIOD = 5;

    public String process(Exchange exchange) {
        File fileInput = exchange.getIn().getBody(File.class);
        if(checkFileName(fileInput.getAbsolutePath())) {
            return null;
        }
        System.out.println("Update file: " + fileInput.getAbsolutePath());
        List<String> relativePath = FileUtil.getRelativePath(dirPath, fileInput.getAbsolutePath());
        byte[] content = readAllBytes(fileInput);
        int fileSize = content.length;
        LocalDateTime lastModifiedTime = FileUtil.getLastModifiedTime(fileInput.getAbsolutePath());

        return new Gson().toJson(
                new FileDto(
                        relativePath,
                        fileSize,
                        content,
                        lastModifiedTime));
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
                        .isAfter(LocalDateTime.now().minusSeconds(DELAY_PERIOD));
    }

}

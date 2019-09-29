package ru.otus.salamandra.app.sync;

import com.google.gson.Gson;
import ru.otus.salamandra.app.store.FileNameStorage;
import ru.otus.salamandra.app.store.SessionStore;
import ru.otus.salamandra.dto.FileDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SyncProcessor {

    public void perform(String message) {
        FileDto fileDto = new Gson().fromJson(message, FileDto.class);
        Path filePath = Paths.get(SessionStore.getInstance().getBaseDirPath(),
                getAggregatedRelativePath(fileDto.getRelativePath()));
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
                Files.createFile(filePath);
                Files.write(filePath, fileDto.getContent());
                FileNameStorage.getInstance().syncUpdate(filePath.toString(), fileDto.getVersion());
                System.out.println("SYNC: Rewrite file: " + filePath);
            } catch (IOException e) {
                System.err.println("Sync saving error: \"" + e.getMessage() + "\"");
            }
        } else {
            try {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                Files.write(filePath, fileDto.getContent());
                FileNameStorage.getInstance().syncUpdate(filePath.toString(), fileDto.getVersion());
                System.out.println("SYNC: Create new file: " + filePath);
            } catch (IOException e) {
                System.err.println("Sync saving error: \"" + e.getMessage() + "\"");
            }
        }
    }

    private String getAggregatedRelativePath(List<String> relativePath) {
        return String.join("\\", relativePath);
    }

}

package ru.otus.salamandra.app.store;

import java.util.HashSet;
import java.util.Set;

public class FileNameStorage {

    private static FileNameStorage instance = new FileNameStorage();
    private final Set<String> fileNames;

    private FileNameStorage() {
        fileNames = new HashSet<>();
    }

    public static synchronized FileNameStorage getInstance() {
        return instance;
    }

    public boolean checkAndStore(String fileName) {
        if (fileNames.contains(fileName)) {
            return true;
        } else {
            fileNames.add(fileName);
            return false;
        }
    }

}

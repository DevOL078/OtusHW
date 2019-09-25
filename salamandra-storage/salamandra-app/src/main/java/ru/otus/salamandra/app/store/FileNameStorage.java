package ru.otus.salamandra.app.store;

import java.util.ArrayList;
import java.util.List;

public class FileNameStorage {

    private static FileNameStorage instance = new FileNameStorage();
    private final List<String> fileNames;

    private FileNameStorage(){
        fileNames = new ArrayList<>();
    }

    public static synchronized FileNameStorage getInstance() {
        return instance;
    }

    public boolean checkAndStore(String fileName) {
        if(fileNames.contains(fileName)) {
            return true;
        } else {
            fileNames.add(fileName);
            return false;
        }
    }

}

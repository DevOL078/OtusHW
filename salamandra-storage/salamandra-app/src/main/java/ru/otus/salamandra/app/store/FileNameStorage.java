package ru.otus.salamandra.app.store;

import java.util.HashMap;
import java.util.Map;

public class FileNameStorage {

    private static final FileNameStorage instance = new FileNameStorage();
    private final Map<String, StorageElement> filesInfo;

    private FileNameStorage() {
        filesInfo = new HashMap<>();
    }

    public static synchronized FileNameStorage getInstance() {
        return instance;
    }

    public void saveOrUpdate(String fileName) {
        if(filesInfo.containsKey(fileName)) {
            StorageElement storageElement = filesInfo.get(fileName);
            storageElement.version++;
        } else {
            filesInfo.put(fileName, new StorageElement(1, true));
        }
    }

    public String fileStatus(String fileName) {
        StorageElement storageElement = filesInfo.get(fileName);
        if(storageElement == null) {
            return "CREATED";
        } else {
            if(storageElement.isNeedToUpdate) {
                return "UPDATED";
            } else {
                storageElement.isNeedToUpdate = true;
                return "SYNC";
            }
        }
    }

    public Integer getVersion(String fileName) {
        if(filesInfo.containsKey(fileName)) {
            return filesInfo.get(fileName).version;
        }
        return null;
    }

    public void syncUpdate(String fileName, int version) {
        if(filesInfo.containsKey(fileName)) {
            StorageElement storageElement = filesInfo.get(fileName);
            storageElement.version = version;
            storageElement.isNeedToUpdate = false;
        } else {
            StorageElement storageElement = new StorageElement(version, false);
            filesInfo.put(fileName, storageElement);
        }
    }

    static class StorageElement {
        private int version;
        private boolean isNeedToUpdate;

        public StorageElement(int version, boolean isNeedToUpdate) {
            this.version = version;
            this.isNeedToUpdate = isNeedToUpdate;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public boolean isNeedToUpdate() {
            return isNeedToUpdate;
        }

        public void setNeedToUpdate(boolean needToUpdate) {
            isNeedToUpdate = needToUpdate;
        }
    }

}

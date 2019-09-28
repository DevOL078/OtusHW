package ru.otus.salamandra.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SyncRequestDto {

    private List<FileProperties> filePropertiesList = new ArrayList<>();
    private String appId;

    public SyncRequestDto() {}

    public SyncRequestDto(String appId) {
        this.appId = appId;
    }

    public void addFileProperties(FileProperties fileProperties) {
        filePropertiesList.add(fileProperties);
    }

    public List<FileProperties> getFilePropertiesList() {
        return new ArrayList<>(filePropertiesList);
    }

    public String getAppId() {
        return appId;
    }

    @Override
    public String toString() {
        return "SyncRequestDto " + filePropertiesList.toString();
    }

    public static class FileProperties {
        private List<String> relativePath;
        private int version;

        public FileProperties(List<String> relativePath, int version) {
            this.relativePath = relativePath;
            this.version = version;
        }

        public List<String> getRelativePath() {
            return relativePath;
        }

        public int getVersion() {
            return version;
        }

        @Override
        public String toString() {
            return String.format("FileProperties [relativePath = %s, version = %d]",
                    relativePath,
                    version);
        }
    }

}

package ru.otus.salamandra.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyncRequestDto {

    private List<FileProperties> filePropertiesList = new ArrayList<>();

    public void addFileProperties(FileProperties fileProperties) {
        filePropertiesList.add(fileProperties);
    }

    public List<FileProperties> getFilePropertiesList() {
        return new ArrayList<>(filePropertiesList);
    }

    @Override
    public String toString() {
        return "SyncRequestDto " + filePropertiesList.toString();
    }

    public static class FileProperties {
        private List<String> relativePath;
        private LocalDateTime lastModifiedTime;

        public FileProperties(List<String> relativePath, LocalDateTime lastModifiedTime) {
            this.relativePath = relativePath;
            this.lastModifiedTime = lastModifiedTime;
        }

        public List<String> getRelativePath() {
            return relativePath;
        }

        public LocalDateTime getLastModifiedTime() {
            return lastModifiedTime;
        }

        @Override
        public String toString() {
            return String.format("FileProperties [relativePath = %s, lastModifiedTime = %s]",
                    relativePath,
                    lastModifiedTime);
        }
    }

}

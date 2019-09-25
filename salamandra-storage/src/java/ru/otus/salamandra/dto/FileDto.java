package ru.otus.salamandra.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FileDto {
    private final byte[] content;
    private final List<String> relativePath;
    private final int fileSize;
    private final LocalDateTime lastModifiedTime;

    public FileDto(List<String> relativePath,
                   int fileSize,
                   byte[] content,
                   LocalDateTime lastModifiedTime) {
        this.content = content;
        this.relativePath = relativePath;
        this.fileSize = fileSize;
        this.lastModifiedTime = lastModifiedTime;
    }

    public byte[] getContent() {
        return content;
    }

    public List<String> getRelativePath() {
        return relativePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }
}

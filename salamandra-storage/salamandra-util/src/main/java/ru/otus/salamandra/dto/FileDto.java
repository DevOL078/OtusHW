package ru.otus.salamandra.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FileDto {
    private final byte[] content;
    private final List<String> relativePath;
    private final int fileSize;
    private final int version;
    private final String login;

    public FileDto(List<String> relativePath,
                   int fileSize,
                   byte[] content,
                   int version,
                   String login) {
        this.content = content;
        this.relativePath = relativePath;
        this.fileSize = fileSize;
        this.version = version;
        this.login = login;
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

    public int getVersion() {
        return version;
    }

    public String getLogin() {
        return login;
    }
}

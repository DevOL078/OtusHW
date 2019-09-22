package ru.otus.salamandra.dto;

import java.util.List;

public class FileDto {
    private final byte[] content;
    private final String fileName;
    private final List<String> subDirs;
    private final int fileSize;

    public FileDto(List<String> subDirs, String fileName, int fileSize, byte[] content) {
        this.content = content;
        this.subDirs = subDirs;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public List<String> getSubDirs() {
        return subDirs;
    }

    public byte[] getContent() {
        return content;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }
}

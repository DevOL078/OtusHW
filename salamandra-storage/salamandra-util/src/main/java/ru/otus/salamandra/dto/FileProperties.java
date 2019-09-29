package ru.otus.salamandra.dto;

import java.util.List;

public class FileProperties {

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

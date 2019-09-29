package ru.otus.salamandra.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    public static LocalDateTime getLastModifiedTime(String filePath) {
        try {
            Path path = Path.of(filePath);
            FileTime lastModifiedFileTime = Files.getLastModifiedTime(path);
            return LocalDateTime.ofInstant(lastModifiedFileTime.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static List<String> getRelativePath(String baseDirPath, String filePath) {
        return Arrays
                .stream(filePath
                        .replace(baseDirPath, "")
                        .replace("\\", "/")
                        .split("/"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

}

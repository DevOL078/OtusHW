package ru.otus.salamandra.app.sync;

import com.google.gson.Gson;
import org.apache.camel.Exchange;
import ru.otus.salamandra.dto.FileDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SyncProcessor {

    private final String dirPath = "C:\\Users\\Dmitry\\Desktop\\Otus\\OtusHW\\salamandra-storage\\salamandra-app\\files";

    public String process(Exchange exchange) {
        System.out.println("Sync process");
        File fileInput = exchange.getIn().getBody(File.class);
        System.out.println("New file: " + fileInput.getAbsolutePath());
        List<String> subDirs = Arrays
                .stream(fileInput.getParent()
                        .replace(dirPath, "")
                        .replace("\\", "/")
                        .split("/"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        String fileName = fileInput.getName();
        byte[] content = readAllBytes(fileInput);
        int fileSize = content.length;

        return new Gson().toJson(new FileDto(subDirs, fileName, fileSize, content));
    }

    private byte[] readAllBytes(File file) {
        try (FileInputStream fs = new FileInputStream(file)) {
            return fs.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

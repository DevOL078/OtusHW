package ru.otus.salamandra.server.processor;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.salamandra.dto.FileDto;
import ru.otus.salamandra.dto.SyncRequestDto;
import ru.otus.salamandra.server.domain.File;
import ru.otus.salamandra.server.domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyncProcessor {

    private final String BASE_DIR_PATH = "C:\\Users\\Dmitry\\Desktop\\Otus\\OtusHW\\salamandra-storage\\salamandra-server\\storage";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void process(SyncRequestDto syncRequestDto, User user) {
        List<File> filesFromDB = user.getFiles();
        List<SyncRequestDto.FileProperties> filePropertiesList = syncRequestDto.getFilePropertiesList();

        List<File> filesToSend = getFilesToSend(filesFromDB, filePropertiesList);
        System.out.println(filesToSend);
        filesToSend.forEach(f -> sendFiles(f, syncRequestDto.getAppId()));
    }

    private String getAggregatedRelativePath(List<String> relativePath) {
        return String.join("\\", relativePath);
    }

    private List<File> getFilesToSend(List<File> filesFromDB, List<SyncRequestDto.FileProperties> filePropsFromClient) {
        List<String> fileNamesFromClient = filePropsFromClient.stream()
                .map(fp -> getAggregatedRelativePath(fp.getRelativePath()))
                .collect(Collectors.toList());

        List<File> filesToSend = new ArrayList<>();

        for (File file : filesFromDB) {
            if (fileNamesFromClient.contains(file.getFileName())) {
                //Файл есть и на клиенте, и на сервере
                //Сравниваем время изменения; если на сервере файл менялся позже -> отправляем его на клиент
                SyncRequestDto.FileProperties fileProperties = filePropsFromClient.get(
                        fileNamesFromClient.indexOf(file.getFileName()));
                if (file.getLastModifiedTime().isAfter(fileProperties.getLastModifiedTime())) {
                    filesToSend.add(file);
                }
            } else {
                //Файл есть только в БД -> отправляем его на клиент
                filesToSend.add(file);
            }
        }

        return filesToSend;
    }

    private void sendFiles(File file, String appId) {
        List<String> relativePath = Arrays.asList(file.getFileName().split("/"));

        FileDto fileDto = new FileDto(
                relativePath,
                file.getFileSize(),
                getBytes(file),
                file.getLastModifiedTime(),
                file.getUser().getLogin()
        );

        System.out.println("Sending to Rabbit");
        String routingKey = file.getUser().getLogin() + "/" + appId;
        rabbitTemplate.convertAndSend("sync", routingKey, new Gson().toJson(fileDto));
    }

    private byte[] getBytes(File file) {
        try {
            Path path = Paths.get(BASE_DIR_PATH, file.getUser().getStorageDir(), file.getFileName());
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("File reading error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

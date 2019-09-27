package ru.otus.salamandra.server.processor;

import com.google.gson.Gson;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import ru.otus.salamandra.dto.FileDto;
import ru.otus.salamandra.server.domain.File;
import ru.otus.salamandra.server.domain.User;
import ru.otus.salamandra.server.domain.repo.FileRepository;
import ru.otus.salamandra.server.domain.repo.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
public class FileProcessor {

    private final String storagePath = "C:\\Users\\Dmitry\\Desktop\\Otus\\OtusHW\\salamandra-storage\\salamandra-server\\storage";

    private FileRepository fileRepository;
    private UserRepository userRepository;

    public FileProcessor(FileRepository fileRepository,
                         UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    public void process(Exchange exchange) throws IOException {
        String json = exchange.getIn().getBody(String.class);
        FileDto fileDto = new Gson().fromJson(json, FileDto.class);
        saveFile(fileDto);
    }

    private void saveFile(FileDto fileDto) throws IOException {
        Optional<User> userOpt = userRepository.findByLogin(fileDto.getLogin());
        if(userOpt.isEmpty()) {
            System.err.println(String.format("User with login \"%s\" not found", fileDto.getLogin()));
            return;
        }
        User user = userOpt.get();

        String relativePath = getRelativePath(fileDto.getRelativePath());
        Path filePath = Path.of(storagePath, user.getStorageDir(), relativePath);
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
            Files.write(filePath, fileDto.getContent());

            File file = new File(
                    relativePath,
                    fileDto.getFileSize(),
                    fileDto.getLastModifiedTime(),
                    user
            );

            fileRepository.save(file);

            System.out.println("Save new file: " + filePath);
        } else {
            Optional<File> fileFromDBOpt = fileRepository.findByUserIdAndFileName(user.getId(), relativePath);
            if(fileFromDBOpt.isPresent()) {
                File fileFromDB = fileFromDBOpt.get();
                if(fileDto.getLastModifiedTime().isAfter(fileFromDB.getLastModifiedTime())) {
                    //если файл редактировался после сохранения на сервере -> перезаписываем
                    Files.delete(filePath);
                    Files.createFile(filePath);
                    Files.write(filePath, fileDto.getContent());

                    fileFromDB.setLastModifiedTime(fileDto.getLastModifiedTime());
                    fileRepository.save(fileFromDB);
                    System.out.println("File update: " + filePath);
                }
            } else {
                //Файл есть в локальном хранилище, но его нет в бд
                // -> перезаписываем локально, сохраняем новый объект в БД
                System.out.println("File " + filePath + " update");

                Files.delete(filePath);
                Files.createFile(filePath);
                Files.write(filePath, fileDto.getContent());

                File newFile = new File(
                        relativePath,
                        fileDto.getFileSize(),
                        fileDto.getLastModifiedTime(),
                        user
                );
                fileRepository.save(newFile);
            }
        }
    }

    private String getRelativePath(List<String> relativePath) {
        return String.join("\\", relativePath);
    }

}

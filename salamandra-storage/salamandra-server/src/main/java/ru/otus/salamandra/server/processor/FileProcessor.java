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
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
        String relativePath = getRelativePath(fileDto.getRelativePath());
        Path filePath = Path.of(storagePath, relativePath);
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
            Files.write(filePath, fileDto.getContent());

            //Test user
            Optional<User> userOpt = userRepository.findByLogin("test");
            User user;
            if(userOpt.isPresent()) {
                user = userOpt.get();
            } else {
                user = new User(
                        "test",
                        "12345",
                        "testDir"
                );
                userRepository.save(user);
            }

            File file = new File(
                    getRelativePath(fileDto.getRelativePath()),
                    fileDto.getFileSize(),
                    fileDto.getLastModifiedTime(),
                    user
            );

            fileRepository.save(file);

            System.out.println(file);
        } else {
            File fileFromDB = fileRepository.findByFileName(relativePath)
                    .orElseThrow(() ->
                            new IllegalStateException("File is in local storage, but no in DB"));
            if(fileDto.getLastModifiedTime().isAfter(fileFromDB.getLastModifiedTime())) {
                //если файл редактировался после сохранения на сервере -> перезаписываем
                Files.delete(filePath);
                Files.createFile(filePath);
                Files.write(filePath, fileDto.getContent());

                fileFromDB.setLastModifiedTime(fileDto.getLastModifiedTime());
                fileRepository.save(fileFromDB);
                System.out.println("File update: " + filePath);
            }
        }
    }

    private String getRelativePath(List<String> relativePath) {
        return String.join("\\", relativePath);
    }

}

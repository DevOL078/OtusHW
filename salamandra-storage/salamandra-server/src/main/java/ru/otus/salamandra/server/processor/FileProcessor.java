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
        Path filePath = Path.of(storagePath, fileDto.getFileName());
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            Files.write(filePath, fileDto.getContent());

            //Test user
            User user = new User(
                    "test",
                    "12345",
                    "testDir"
            );
            userRepository.save(user);

            fileRepository.save(new File(
                    fileDto.getFileName(),
                    fileDto.getFileSize(),
                    user
            ));
        }
    }

}

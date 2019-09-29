package ru.otus.salamandra.server.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.salamandra.dto.SyncRequestDto;
import ru.otus.salamandra.server.domain.User;
import ru.otus.salamandra.server.domain.repo.UserRepository;
import ru.otus.salamandra.server.processor.SyncProcessor;

import java.util.Optional;

@Controller
@RequestMapping("/sync")
public class SyncController {

    private SyncProcessor syncProcessor;

    private UserRepository userRepository;

    public SyncController(SyncProcessor syncProcessor, UserRepository userRepository) {
        this.syncProcessor = syncProcessor;
        this.userRepository = userRepository;
    }

    @PostMapping("/{login}")
    public ResponseEntity<String> syncRequest(@PathVariable String login, @RequestBody String json) {

        System.out.println("Request to sync");
        SyncRequestDto syncRequestDto = new Gson().fromJson(json, SyncRequestDto.class);
        System.out.println(syncRequestDto);

        Optional<User> userOpt = userRepository.findByLogin(login);
        if(userOpt.isEmpty()) {
            System.err.println("User hasn't been find: " + login);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("User with login %s hasn't been found", login));
        }
        User user = userOpt.get();
        syncProcessor.process(syncRequestDto, user);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}

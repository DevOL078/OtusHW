package ru.otus.salamandra.server.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.otus.salamandra.dto.UserDto;
import ru.otus.salamandra.server.domain.User;
import ru.otus.salamandra.server.domain.repo.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody String json) {
        UserDto userDto = new Gson().fromJson(json, UserDto.class);
        System.out.println("Sign in: " + userDto);

        Optional<User> userOpt = userRepository.findByLoginAndEncryptedPassword(
                userDto.getLogin(), userDto.getPassword());
        if(userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.of(Optional.of(new Gson().toJson("Success")));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody String json) {
        UserDto userDto = new Gson().fromJson(json, UserDto.class);
        System.out.println("Register: " + userDto);

        Optional<User> userOpt = userRepository.findByLogin(userDto.getLogin());
        if(userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User(
                userDto.getLogin(),
                userDto.getPassword(),
                UUID.randomUUID().toString()
        );
        userRepository.save(user);

        return ResponseEntity.of(Optional.of(new Gson().toJson("Success")));
    }

}

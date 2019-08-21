package ru.otus.di.controller;

import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.*;
import ru.otus.di.utils.AddressDataSetTypeAdapter;
import ru.otus.di.utils.UserSerializationExclusionStrategy;
import ru.otus.hibernate.dao.AddressDataSet;
import ru.otus.hibernate.dao.User;
import ru.otus.hibernate.service.DBService;

@RestController("/users")
public class UserController {

    private DBService<User> userService;
    private GsonBuilder gsonBuilder;

    public UserController(DBService<User> userService) {
        this.userService = userService;
        this.gsonBuilder = new GsonBuilder()
                .addSerializationExclusionStrategy(new UserSerializationExclusionStrategy())
                .registerTypeAdapter(AddressDataSet.class, new AddressDataSetTypeAdapter());
    }

    @GetMapping
    public String getUsers() {
        return gsonBuilder.create().toJson(userService.getAll());
    }

    @PostMapping
    @ResponseBody
    public String addUser(@RequestParam String name,
                          @RequestParam String address,
                          @RequestParam int age) {
        User user = new User(name, age);
        AddressDataSet addressDataSet = new AddressDataSet(address);
        addressDataSet.setUser(user);
        user.setAddress(addressDataSet);
        userService.save(user);

        return "New user: " + name;
    }

}

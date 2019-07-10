package ru.otus.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Person personFrom = new Person(
                "David",
                25,
                182.5,
                true,
                new Person[]{new Person(
                        "Man",
                        23,
                        180.0,
                        true,
                        new Person[]{},
                        new int[]{5, 3},
                        Arrays.asList("fat", "strong")
                )},
                new int[]{5, 5, 4},
                Arrays.asList("fat", "strong")
        );

        JSONSerializer serializer = new JSONSerializer();
        String json = serializer.toJson(personFrom);
        System.out.println(json);
        Gson gson = new GsonBuilder().create();
        Person personTo = gson.fromJson(json, Person.class);
        System.out.print("PersonFrom equals PersonTo: ");
        System.out.println(personFrom.equals(personTo));
    }

}

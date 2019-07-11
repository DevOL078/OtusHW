package ru.otus.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class JSONSerializerTest {

    @Test
    public void toJson() {
        Person personFrom = new Person(
                "David",
                25,
                182.5,
                true,
                null,
                new Person[]{new Person(
                        "Man",
                        23,
                        180.0,
                        true,
                        null,
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
        assertEquals(personFrom, personTo);
    }
}
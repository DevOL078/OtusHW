package ru.otus.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JSONSerializerTest {

    @Test
    public void toJsonPersonTest() {
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

    @Test
    public void toJsonCustomTest() {
        JSONSerializer serializer = new JSONSerializer();
        Gson gson = new GsonBuilder().create();

        assertEquals(gson.toJson(null), serializer.toJson(null));
        assertEquals(gson.toJson((byte)1), serializer.toJson((byte)1));
        assertEquals(gson.toJson((short)1f), serializer.toJson((short)1f));
        assertEquals(gson.toJson(1), serializer.toJson(1));
        assertEquals(gson.toJson(1L), serializer.toJson(1L));
        assertEquals(gson.toJson(1f), serializer.toJson(1f));
        assertEquals(gson.toJson(1d), serializer.toJson(1d));
        assertEquals(gson.toJson("aaa"), serializer.toJson("aaa"));
        assertEquals(gson.toJson('a'), serializer.toJson('a'));
        assertEquals(gson.toJson(true), serializer.toJson(true));
        assertEquals(gson.toJson(false), serializer.toJson(false));
        assertEquals(gson.toJson(new int[] {1, 2, 3}), serializer.toJson(new int[] {1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2 ,3)), serializer.toJson(List.of(1, 2 ,3)));
        assertEquals(gson.toJson(Collections.singletonList(1)), serializer.toJson(Collections.singletonList(1)));
    }
}
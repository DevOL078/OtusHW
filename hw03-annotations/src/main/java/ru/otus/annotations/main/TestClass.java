package ru.otus.annotations.main;

import ru.otus.annotations.framework.annotations.After;
import ru.otus.annotations.framework.annotations.Before;
import ru.otus.annotations.framework.annotations.Test;

import java.util.Arrays;

public class TestClass {

    @Before
    public void before1() {
        System.out.println("BEFORE1");
    }

    @Before
    public void before2() {
        System.out.println("BEFORE2");
    }

    @Test
    public void test1() {
        System.out.println("TEST1");
    }

    @Test
    public void test2() {
        System.out.println("TEST2");
    }

    @Test
    public void testException() {
        System.out.println("TEST EXCEPTION");
        String[] words = {"abc", "12", "qwerty", null, null};
        Object[] wordsLengths = Arrays.stream(words).map(String::length).toArray();
    }

    @After
    public void after() {
        System.out.println("AFTER");
    }

}

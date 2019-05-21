package ru.otus.annotations.main;

import ru.otus.annotations.framework.runner.TestRunner;

public class TestFrameworkMain {

    public static void main(String[] args) {
        String className = "ru.otus.annotations.main.TestClass";
        try {
            TestRunner runner = new TestRunner(className);
            runner.run();
        } catch (ClassNotFoundException e) {
            System.out.println("Class " + className + " not found");
        }
    }

}

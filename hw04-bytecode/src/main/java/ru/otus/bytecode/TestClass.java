package ru.otus.bytecode;

import ru.otus.bytecode.annotations.Log;
import ru.otus.bytecode.annotations.WithLog;

@WithLog
class TestClass {

    private int resultInt = 0;
    private double resultDouble = 0;
    private String resultString = "";

    @Log
    void sumInt(Integer a, Integer b) {
        System.out.println("Integers");
        this.resultInt = a + b;
    }

    //Тестовый метод без @Log
    void sumDouble(Double a, Double b) {
        System.out.println("Doubles");
        this.resultDouble = a + b;
    }

    @Log
    void sumString(String a, String b) {
        System.out.println("Strings");
        this.resultString = a + b;
    }

    @Log
    void printResult() {
        System.out.println(resultInt);
        System.out.println(resultDouble);
        System.out.println(resultString);
    }

}

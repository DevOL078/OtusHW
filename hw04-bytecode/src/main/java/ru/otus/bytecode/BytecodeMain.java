package ru.otus.bytecode;

public class BytecodeMain {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.sumInt(2, 5);
        testClass.sumDouble(10.0, 15.0);
        testClass.sumString("ABC", "DEF");
        testClass.printResult();
    }

}

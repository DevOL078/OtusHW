package ru.otus.bytecode.done;

public class BytecodeMain {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.sum(2, 5);
        testClass.printResult("Answer: ");
    }

}

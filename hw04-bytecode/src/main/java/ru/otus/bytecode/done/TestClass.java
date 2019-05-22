package ru.otus.bytecode.done;

@WithLog
public class TestClass {

    private int result = 0;

    @Log
    public void sum(int a, int b) {
        System.out.println("SUM");
        this.result = a + b;
    }

    @Log
    public void printResult(String prefix) {
        System.out.println(prefix + result);
    }

}

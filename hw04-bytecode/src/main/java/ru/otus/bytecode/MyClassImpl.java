package ru.otus.bytecode;



public class MyClassImpl {

    public void secureAccess(String param) {
        System.out.println("secureAccess, param:" + param);
    }

    public void sum(double a, String b, int c, float f, boolean bool, char ch, long l) {
        System.out.println("SUM");
    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
package ru.otus.bytecode;

public class InstrumentationDemo {

    public static void main(String[] args) {
        MyClassImpl myClass = new MyClassImpl();
        myClass.secureAccess("Security Param");
    }

}

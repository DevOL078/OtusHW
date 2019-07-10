package ru.otus.json;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Person {
    private final String name;
    private final int age;
    private final double height;
    private final boolean gender;
    private final Person[] contacts;
    private final int[] marks;
    private final List<String> specifications;

    Person(String name,
           int age,
           double height,
           boolean gender,
           Person[] contacts,
           int[] marks,
           List<String> specifications) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.gender = gender;
        this.contacts = contacts;
        this.marks = marks;
        this.specifications = specifications;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Person that = (Person) o;

        if(!Objects.equals(this.name, that.name)) return false;
        if(this.age != that.age) return false;
        if(this.gender != that.gender) return false;
        if(this.height != that.height) return false;
        if(!Arrays.equals(this.contacts, that.contacts)) return false;
        if(!Arrays.equals(this.marks, that.marks)) return false;
        return this.specifications.equals(that.specifications);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name=" + this.name +
                ", age=" + this.age +
                ", gender=" + this.gender +
                ", height=" + this.height +
                ", marks=" + Arrays.toString(this.marks) +
                ", contacts=" + Arrays.toString(this.contacts) +
                ", specifications=" + this.specifications +
                "}";
    }

}

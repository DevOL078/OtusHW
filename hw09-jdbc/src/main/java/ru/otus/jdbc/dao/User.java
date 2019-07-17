package ru.otus.jdbc.dao;

import ru.otus.jdbc.annotation.Id;

public class User {

    @Id
    private long id;

    private String name;

    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !other.getClass().equals(User.class)) {
            return false;
        }
        if (this == other) return true;

        User that = (User) other;
        if (this.id != that.getId()) return false;
        if (!this.name.equals(that.getName())) return false;
        return this.age == that.getAge();
    }

    @Override
    public String toString() {
        return "User(id=" + id +
                ", name=" + name +
                ", age=" + age + ")";
    }

}

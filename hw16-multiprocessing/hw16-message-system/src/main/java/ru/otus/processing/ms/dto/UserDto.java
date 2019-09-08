package ru.otus.processing.ms.dto;

public class UserDto {
    private final String name;
    private final String address;
    private final int age;

    public UserDto(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "UserDto [name = " + this.name +
                ", address = " + this.address +
                ", age = " + this.age + "]";
    }
}

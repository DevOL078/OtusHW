package ru.otus.salamandra.dto;

public class UserDto {

    private final String login;
    private final String password;

    public UserDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDto(){
        login = null;
        password = null;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("UserDto [login = %s, password %s]", login, password);
    }
}

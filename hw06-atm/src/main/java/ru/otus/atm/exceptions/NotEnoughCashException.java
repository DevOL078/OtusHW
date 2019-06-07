package ru.otus.atm.exceptions;

public class NotEnoughCashException extends RuntimeException {

    public NotEnoughCashException(String message) {
        super(message);
    }

}

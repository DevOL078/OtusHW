package ru.otus.atm;

public interface ICell {

    void addCash(int billCount);

    void removeCash(int billCount);

    int getBalance();

    CashType getCashType();

}

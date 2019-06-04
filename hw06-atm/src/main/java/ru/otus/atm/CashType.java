package ru.otus.atm;

public enum CashType {

    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDREDS(500),
    ONE_THOUSAND(1000),
    FIVE_THOUSAND(5000);

    private int number;

    CashType(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

}

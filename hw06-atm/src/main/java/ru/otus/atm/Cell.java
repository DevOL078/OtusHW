package ru.otus.atm;

import ru.otus.atm.exceptions.NotEnoughCashException;

public class Cell implements ICell {

    private CashType cashType;
    private int billCount = 0;

    public Cell(CashType cashType) {
        this.cashType = cashType;
    }

    @Override
    public void addCash(int billCount) {
        this.billCount += billCount;
    }

    @Override
    public void removeCash(int billCount) {
        int newCount = this.billCount - billCount;
        if (newCount < 0) {
            throw new NotEnoughCashException("Impossible to remove cash from cell " + cashType.getNumber());
        }
        this.billCount = newCount;
    }

    @Override
    public int getBalance() {
        return cashType.getNumber() * billCount;
    }

    @Override
    public CashType getCashType() {
        return cashType;
    }

}

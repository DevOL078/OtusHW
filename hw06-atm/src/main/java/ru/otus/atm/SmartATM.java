package ru.otus.atm;

import java.util.List;

public class SmartATM extends AbstractATM {

    private AbstractCellWrapper firstWrapper;

    @Override
    public void setFirstWrapper(AbstractCellWrapper wrapper) {
        this.firstWrapper = wrapper;
    }

    @Override
    public void addCash(List<CashType> cashList) {
        firstWrapper.addCash(cashList);
    }

    @Override
    public List<CashType> giveCash(int sum) {
        return firstWrapper.giveCash(sum);
    }

    @Override
    public int getTotalBalance() {
        int balance = 0;
        AbstractCellWrapper currentWrapper = firstWrapper;
        balance += currentWrapper.getCell().getBalance();
        while (currentWrapper.next != null) {
            currentWrapper = currentWrapper.next;
            balance += currentWrapper.getCell().getBalance();
        }
        return balance;
    }
}

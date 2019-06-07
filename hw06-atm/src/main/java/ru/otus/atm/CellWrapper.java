package ru.otus.atm;

import java.util.ArrayList;
import java.util.List;

public class CellWrapper extends AbstractCellWrapper {

    public CellWrapper(ICell cell) {
        super(cell);
    }

    @Override
    public List<CashType> giveCash(int sum) {
        int balance = this.cell.getBalance();
        CashType cashType = this.cell.getCashType();
        int cashCount = 0;
        if (balance != 0) {
            if (sum <= balance) {
                cashCount = sum / cashType.getNumber();
            } else {
                cashCount = balance / cashType.getNumber();
            }
        }

        List<CashType> cash;
        int newSum = sum - cashCount * cashType.getNumber();
        if (this.next != null && newSum != 0) {
            cash = this.next.giveCash(newSum);
        } else {
            if (newSum != 0) {
                throw new IllegalStateException("Impossible to give cash");
            }
            cash = new ArrayList<>();
        }
        for (int i = 0; i < cashCount; ++i) {
            cash.add(cashType);
        }
        this.cell.removeCash(cashCount);

        return cash;
    }

    @Override
    public void addCash(List<CashType> cashList) {
        CashType cashType = this.cell.getCashType();
        List<CashType> listOfCashType = new ArrayList<>();
        for (CashType type : cashList) {
            if (type == cashType) {
                listOfCashType.add(type);
            }
        }
        this.cell.addCash(listOfCashType.size());
        cashList.removeAll(listOfCashType);
        if (this.next != null) {
            this.next.addCash(cashList);
        } else {
            if(!cashList.isEmpty()) {
                throw new IllegalStateException("Illegal cash type: " + cashList.get(0));
            }
        }
    }
}

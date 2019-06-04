package ru.otus.atm;

import java.util.List;

public abstract class AbstractATM {

    public abstract void setFirstWrapper(AbstractCellWrapper wrapper);

    public abstract void addCash(List<CashType> cashList);

    public abstract List<CashType> giveCash(int sum);

    public abstract int getTotalBalance();

}

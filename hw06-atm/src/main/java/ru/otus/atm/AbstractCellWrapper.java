package ru.otus.atm;

import java.util.List;

public abstract class AbstractCellWrapper {

    ICell cell;
    AbstractCellWrapper next;

    AbstractCellWrapper(ICell cell) {
        this.cell = cell;
    }

    public void setNext(AbstractCellWrapper wrapper) {
        this.next = wrapper;
    }

    public AbstractCellWrapper getNext() {
        return this.next;
    }

    public ICell getCell() {
        return this.cell;
    }

    public abstract List<CashType> giveCash(int sum);

    public abstract void addCash(List<CashType> cashList);


}

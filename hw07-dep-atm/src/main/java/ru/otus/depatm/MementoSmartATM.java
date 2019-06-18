package ru.otus.depatm;

import ru.otus.atm.*;

import java.util.ArrayList;
import java.util.List;

class MementoSmartATM extends SmartATM {

    Memento save() {
        return new Memento(this.firstWrapper);
    }

    void restore(Memento memento) {
        this.firstWrapper = memento.getWrapper();
    }

    static class Memento {

        private AbstractCellWrapper wrapper;

        Memento(AbstractCellWrapper wrapper) {
            CellWrapperFabric fabric = new CellWrapperFabric();
            while (wrapper != null) {
                CashType cashType = wrapper.getCell().getCashType();
                int balance = wrapper.getCell().getBalance() / cashType.getNumber();
                AbstractCellWrapper copyWrapper = fabric.create(cashType);
                List<CashType> cash = new ArrayList<>();
                while (balance > 0) {
                    cash.add(cashType);
                    balance--;
                }
                copyWrapper.addCash(cash);

                if (this.wrapper == null) {
                    this.wrapper = copyWrapper;
                } else {
                    AbstractCellWrapper lastWrapper = this.wrapper;
                    while (lastWrapper.getNext() != null) {
                        lastWrapper = lastWrapper.getNext();
                    }
                    lastWrapper.setNext(copyWrapper);
                }

                wrapper = wrapper.getNext();
            }
        }

        AbstractCellWrapper getWrapper() {
            return this.wrapper;
        }

    }

}

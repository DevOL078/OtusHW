package ru.otus.depatm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ATMDepartment {

    private Map<MementoSmartATM, MementoSmartATM.Memento> mementoMap = new HashMap<>();

    void addATM(MementoSmartATM atm) {
        if (atm == null) {
            throw new NullPointerException("ATM is null");
        }
        if (mementoMap.containsKey(atm)) {
            throw new IllegalStateException("This depatm already in department");
        }
        MementoSmartATM.Memento memento = atm.save();
        mementoMap.put(atm, memento);
    }

    public void removeATM(MementoSmartATM atm) {
        if (atm == null) {
            throw new NullPointerException("ATM is null");
        }
        mementoMap.remove(atm);
    }

    int getTotalDepartmentBalance() {
        final AtomicInteger balance = new AtomicInteger(0);
        mementoMap.keySet().forEach(m -> balance.set(balance.get() + m.getBalance()));
        return balance.get();
    }

    void resetATMs() {
        mementoMap.forEach(MementoSmartATM::restore);
    }

}

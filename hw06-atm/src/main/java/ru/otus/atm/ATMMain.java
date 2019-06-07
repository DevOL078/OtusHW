package ru.otus.atm;

import java.util.ArrayList;
import java.util.List;

public class ATMMain {

    public static void main(String[] args) {
        AbstractATM atm = new SmartATM();

        CellWrapperChainBuilder wrapperBuilder = new CellWrapperChainBuilder(CashType.FIVE_THOUSAND);
        wrapperBuilder.next(CashType.FIVE_THOUSAND)
                .next(CashType.ONE_THOUSAND)
                .next(CashType.FIVE_HUNDREDS)
                .next(CashType.ONE_HUNDRED);
               // .next(CashType.FIFTY);

        atm.setFirstWrapper(wrapperBuilder.getFirstWrapper());

        List<CashType> cash = new ArrayList<>();
        cash.add(CashType.FIVE_THOUSAND);
        cash.add(CashType.FIVE_THOUSAND);
        cash.add(CashType.FIVE_THOUSAND);
        cash.add(CashType.FIVE_THOUSAND);
        cash.add(CashType.ONE_THOUSAND);
        cash.add(CashType.ONE_THOUSAND);
        cash.add(CashType.ONE_THOUSAND);
        cash.add(CashType.FIVE_HUNDREDS);
        cash.add(CashType.FIVE_HUNDREDS);
        cash.add(CashType.FIVE_HUNDREDS);
        cash.add(CashType.ONE_HUNDRED);
        cash.add(CashType.ONE_HUNDRED);
        cash.add(CashType.ONE_HUNDRED);
        cash.add(CashType.ONE_HUNDRED);
        cash.add(CashType.ONE_HUNDRED);
        cash.add(CashType.FIFTY);
        cash.add(CashType.FIFTY);
        cash.add(CashType.FIFTY);
        cash.add(CashType.FIFTY);
        cash.add(CashType.FIFTY);
        cash.add(CashType.FIFTY);

        atm.addCash(cash);

        System.out.println("Start balance: " + atm.getTotalBalance());

        List<CashType> cashFormATM = atm.giveCash(8750);
        System.out.println(cashFormATM);

        System.out.println("New balance: " + atm.getTotalBalance());
    }

}

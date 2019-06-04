package ru.otus.atm;

import java.util.ArrayList;
import java.util.List;

public class ATMMain {

    public static void main(String[] args) {
        AbstractATM atm = new SmartATM();

        AbstractCellWrapper wrapper5000 = new CellWrapper(
                new Cell(CashType.FIVE_THOUSAND));
        AbstractCellWrapper wrapper1000 = new CellWrapper(
                new Cell(CashType.ONE_THOUSAND));
        AbstractCellWrapper wrapper500 = new CellWrapper(
                new Cell(CashType.FIVE_HUNDREDS));
        AbstractCellWrapper wrapper100 = new CellWrapper(
                new Cell(CashType.ONE_HUNDRED));
        AbstractCellWrapper wrapper50 = new CellWrapper(
                new Cell(CashType.FIFTY));

        wrapper5000.setNext(wrapper1000);
        wrapper1000.setNext(wrapper500);
        wrapper500.setNext(wrapper100);
        wrapper100.setNext(wrapper50);

        atm.setFirstWrapper(wrapper5000);

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

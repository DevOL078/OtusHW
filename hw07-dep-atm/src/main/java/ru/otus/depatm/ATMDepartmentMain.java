package ru.otus.depatm;

import ru.otus.atm.CashType;
import ru.otus.atm.CellWrapperChainBuilder;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartmentMain {

    public static void main(String[] args) {
        MementoSmartATM atm1 = createATM();
        MementoSmartATM atm2 = createATM();
        MementoSmartATM atm3 = createATM();
        System.out.println("Start balance in atm1: " + atm1.getBalance());
        System.out.println("Start balance in atm2: " + atm2.getBalance());
        System.out.println("Start balance in atm3: " + atm3.getBalance());

        ATMDepartment department = new ATMDepartment();
        department.addATM(atm1);
        department.addATM(atm2);
        department.addATM(atm3);

        System.out.println("Total balance in department: " + department.getTotalDepartmentBalance());

        List<CashType> cashFromAtm1 = atm1.giveCash(2500);
        List<CashType> cashFromAtm2 = atm2.giveCash(6000);

        System.out.println("Cash from atm1: " + cashFromAtm1);
        System.out.println("Cash from atm2: " + cashFromAtm2);

        System.out.println("Balance in atm1: " + atm1.getBalance());
        System.out.println("Balance in atm2: " + atm2.getBalance());
        System.out.println("Balance in atm3: " + atm3.getBalance());

        System.out.println("Total balance in department: " + department.getTotalDepartmentBalance());

        department.resetATMs();

        System.out.println("Balance in atm1: " + atm1.getBalance());
        System.out.println("Balance in atm2: " + atm2.getBalance());
        System.out.println("Balance in atm3: " + atm3.getBalance());

        System.out.println("Total balance in department: " + department.getTotalDepartmentBalance());
    }

    private static MementoSmartATM createATM() {
        MementoSmartATM atm = new MementoSmartATM();

        CellWrapperChainBuilder wrapperBuilder = new CellWrapperChainBuilder();
        wrapperBuilder.first(CashType.FIVE_THOUSAND)
                .next(CashType.ONE_THOUSAND)
                .next(CashType.FIVE_HUNDREDS)
                .next(CashType.ONE_HUNDRED)
                .next(CashType.FIFTY);

        atm.setFirstWrapper(wrapperBuilder.getFirstWrapper());

        List<CashType> cash = new ArrayList<>();
        cash.add(CashType.FIVE_THOUSAND);
        cash.add(CashType.FIVE_THOUSAND);
        cash.add(CashType.ONE_THOUSAND);
        cash.add(CashType.ONE_THOUSAND);
        cash.add(CashType.FIVE_HUNDREDS);
        cash.add(CashType.FIVE_HUNDREDS);
        cash.add(CashType.ONE_HUNDRED);
        cash.add(CashType.ONE_HUNDRED);
        cash.add(CashType.FIFTY);
        cash.add(CashType.FIFTY);

        atm.addCash(cash);

        return atm;
    }

}

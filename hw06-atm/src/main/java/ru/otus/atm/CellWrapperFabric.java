package ru.otus.atm;

public class CellWrapperFabric {

    public CellWrapper create(CashType type) {
        return new CellWrapper(new Cell(type));
    }

}

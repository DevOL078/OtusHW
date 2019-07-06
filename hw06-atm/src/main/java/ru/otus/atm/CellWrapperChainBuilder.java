package ru.otus.atm;

public class CellWrapperChainBuilder {

    private CellWrapperFabric fabric = new CellWrapperFabric();
    private AbstractCellWrapper firstWrapper;
    private AbstractCellWrapper lastWrapper;

    public CellWrapperChainBuilder first(CashType type) {
        if (type == null) {
            throw new NullPointerException("Cash type is null");
        }
        AbstractCellWrapper cellWrapper = fabric.create(type);
        this.firstWrapper = cellWrapper;
        this.lastWrapper = cellWrapper;
        return this;
    }

    public CellWrapperChainBuilder next(CashType type) {
        if (type == null) {
            throw new NullPointerException("Cash type is null");
        }
        if (this.firstWrapper == null) {
            throw new NullPointerException("No wrappers in builder");
        }
        AbstractCellWrapper cellWrapper = fabric.create(type);
        this.lastWrapper.setNext(cellWrapper);
        this.lastWrapper = cellWrapper;
        return this;
    }

    public AbstractCellWrapper getFirstWrapper() {
        return this.firstWrapper;
    }

}

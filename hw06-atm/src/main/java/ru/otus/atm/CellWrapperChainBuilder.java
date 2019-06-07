package ru.otus.atm;

public class CellWrapperChainBuilder {

    private CellWrapperFabric fabric = new CellWrapperFabric();
    private AbstractCellWrapper firstWrapper;
    private AbstractCellWrapper lastWrapper;

    public CellWrapperChainBuilder(CashType type) {
        if(type == null)
            throw new NullPointerException("Cash type is null");
        AbstractCellWrapper cellWrapper = fabric.create(type);
        this.firstWrapper = cellWrapper;
        this.lastWrapper = cellWrapper;
    }

    public CellWrapperChainBuilder next(CashType type) {
        if(type == null)
            throw new NullPointerException("Cash type is null");
        AbstractCellWrapper cellWrapper = fabric.create(type);
        this.lastWrapper.setNext(cellWrapper);
        this.lastWrapper = cellWrapper;
        return this;
    }

    public AbstractCellWrapper getFirstWrapper() {
        return this.firstWrapper;
    }

}

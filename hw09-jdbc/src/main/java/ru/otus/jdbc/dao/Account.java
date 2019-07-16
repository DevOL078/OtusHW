package ru.otus.jdbc.dao;

import ru.otus.jdbc.annotation.Id;

import java.math.BigDecimal;

public class Account {

    @Id
    private long no;
    private String type;
    private BigDecimal rest;


    public Account(String type, BigDecimal rest) {
        this.type = type;
        this.rest = rest;
    }


    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getRest() {
        return rest;
    }

    public void setRest(BigDecimal rest) {
        this.rest = rest;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null || !other.getClass().equals(Account.class)) {
            return false;
        }
        if(this == other) return true;

        Account that = (Account) other;
        if(this.no != that.getNo()) return false;
        if(!this.type.equals(that.getType())) return false;
        return this.rest.equals(that.getRest());
    }

    @Override
    public String toString() {
        return "Account(no=" + no +
                ", type=" + type +
                ", rest=" + rest + ")";
    }
}

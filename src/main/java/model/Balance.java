package model;

import java.sql.Date;
import java.util.Objects;

public class Balance {
    private int id;
    private Date createDate;
    private int debit;
    private int credit;
    private int amount;

    public Balance(int id, Date createDate, int debit, int credit, int amount) {
        this.id = id;
        this.createDate = createDate;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", debit=" + debit +
                ", credit=" + credit +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return id == balance.id && debit == balance.debit && credit == balance.credit && amount == balance.amount && Objects.equals(createDate, balance.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, debit, credit, amount);
    }
}

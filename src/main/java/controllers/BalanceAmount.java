package controllers;

import java.util.Objects;

public class BalanceAmount {
    private String date;
    private int amount;

    public BalanceAmount(String date, int amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceAmount that = (BalanceAmount) o;
        return amount == that.amount && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount);
    }

    @Override
    public String toString() {
        return "BalanceAmount{" +
                "date='" + date + '\'' +
                ", amount=" + amount +
                '}';
    }
}

package model;

import java.sql.Date;
import java.util.Objects;

public class Operation {
    private int id;
    private Article article;
    private int debit;
    private int credit;
    private Date creditDate;
    private Balance balance;

    public Operation(int id, Article article, int debit, int credit, Date creditDate, Balance balance) {
        this.id = id;
        this.article = article;
        this.debit = debit;
        this.credit = credit;
        this.creditDate = creditDate;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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

    public Date getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(Date creditDate) {
        this.creditDate = creditDate;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", article=" + article.toString() +
                ", debit=" + debit +
                ", credit=" + credit +
                ", creditDate=" + creditDate +
                ", balance=" + balance.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return id == operation.id && debit == operation.debit && credit == operation.credit && Objects.equals(article, operation.article) && Objects.equals(creditDate, operation.creditDate) && Objects.equals(balance, operation.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, article, debit, credit, creditDate, balance);
    }
}

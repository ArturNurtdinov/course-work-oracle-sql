package model;

import java.sql.Date;
import java.util.Objects;

public class SimpleOperation {
    private int id;
    private int articleId;
    private int debit;
    private int credit;
    private Date creditDate;
    private int balanceId;

    public SimpleOperation(int id, int articleId, int debit, int credit, Date creditDate, int balanceId) {
        this.id = id;
        this.articleId = articleId;
        this.debit = debit;
        this.credit = credit;
        this.creditDate = creditDate;
        this.balanceId = balanceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
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

    public int getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleOperation that = (SimpleOperation) o;
        return id == that.id && articleId == that.articleId && debit == that.debit && credit == that.credit && balanceId == that.balanceId && Objects.equals(creditDate, that.creditDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleId, debit, credit, creditDate, balanceId);
    }

    @Override
    public String toString() {
        return "SimpleOperation{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", debit=" + debit +
                ", credit=" + credit +
                ", creditDate=" + creditDate +
                ", balanceId=" + balanceId +
                '}';
    }
}

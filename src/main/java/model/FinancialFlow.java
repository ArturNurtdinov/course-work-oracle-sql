package model;

import java.util.Objects;

public class FinancialFlow<T> {
    private T feature;
    private double percent;

    public FinancialFlow(T feature, double percent) {
        this.feature = feature;
        this.percent = percent;
    }

    public T getFeature() {
        return feature;
    }

    public void setFeature(T feature) {
        this.feature = feature;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialFlow<?> that = (FinancialFlow<?>) o;
        return percent == that.percent && Objects.equals(feature, that.feature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feature, percent);
    }

    @Override
    public String toString() {
        return "FinancialFlow{" +
                "feature=" + feature +
                ", percent=" + percent +
                '}';
    }
}

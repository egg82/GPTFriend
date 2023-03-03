package me.egg82.gpt.core;

import java.util.Objects;

public class Weighted<T> {
    private final T item;
    private double weight;

    public Weighted(T item, double weight) {
        if (weight < 0.0d) {
            throw new IllegalArgumentException("weight cannot be < 0 (given: " + weight + ")");
        }

        this.item = item;
        this.weight = weight;
    }

    public T getItem() {
        return item;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0.0d) {
            throw new IllegalArgumentException("weight cannot be < 0 (given: " + weight + ")");
        }

        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weighted<?> weighted = (Weighted<?>) o;
        return Double.compare(weighted.weight, weight) == 0 && Objects.equals(item, weighted.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, weight);
    }

    @Override
    public String toString() {
        return "Weighted{" +
                "item=" + item +
                ", weight=" + weight +
                '}';
    }
}

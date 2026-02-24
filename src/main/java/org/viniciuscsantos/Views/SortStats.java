package org.viniciuscsantos.Views;

public class SortStats {
    private final int[] array;
    private final int comparisons;
    private final int assignments;
    private final Long elapsedNanos;

    public SortStats(int[] array, int comparisons, int assignments, Long elapsedNanos) {
        this.array = array;
        this.comparisons = comparisons;
        this.assignments = assignments;
        this.elapsedNanos = elapsedNanos;
    }

    public int[] getArray() {
        return array;
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getAssignments() {
        return assignments;
    }

    public Long getElapsedNanos() {return elapsedNanos;}
}

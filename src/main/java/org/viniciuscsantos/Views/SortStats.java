package org.viniciuscsantos.Views;

public class SortStats {
    private final int[] array;
    private final int comparisons;
    private final int assignments;

    public SortStats(int[] array, int comparisons, int assignments) {
        this.array = array;
        this.comparisons = comparisons;
        this.assignments = assignments;
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
}

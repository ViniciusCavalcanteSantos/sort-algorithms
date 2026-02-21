package org.viniciuscsantos.Views;

public class SortStats {
    private final int comparisons;
    private final int assignments;

    public SortStats(int comparisons, int assignments) {
        this.comparisons = comparisons;
        this.assignments = assignments;
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getAssignments() {
        return assignments;
    }
}

package org.viniciuscsantos.Views;

public class SortStats {
    private final int[] array;
    private final int comparisons;
    private final int assignments;
    private final Long elapsedNanos;
    private final int[] markers;

    public SortStats(int[] array, int comparisons, int assignments, Long elapsedNanos, int... markers) {
        this.array = array;
        this.comparisons = comparisons;
        this.assignments = assignments;
        this.elapsedNanos = elapsedNanos;
        this.markers = markers;
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

    public int[] getMarkers() {
        return markers;
    }
}

package org.viniciuscsantos.Views;

public class SortStats {
    private final int cycles;
    private final int swaps;

    public SortStats(int cycles, int swaps) {
        this.cycles = cycles;
        this.swaps = swaps;
    }

    public int getSwaps() {
        return swaps;
    }

    public int getCycles() {
        return cycles;
    }
}

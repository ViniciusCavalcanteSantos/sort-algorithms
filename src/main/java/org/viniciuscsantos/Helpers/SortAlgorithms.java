package org.viniciuscsantos.Helpers;

import javafx.application.Platform;
import org.viniciuscsantos.Interfaces.IChartView;
import org.viniciuscsantos.Views.SortStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortAlgorithms {
    static int sleepMillis = 10;

    public static void bubbleSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int cycles = 0;
        int swaps = 0;
        for (int i = 0; i < unsortedArray.length; i++) {
            boolean isSorted = true;

            for (int j = 0; j < unsortedArray.length - i - 1; j++) {
                int current = unsortedArray[j];
                int next = unsortedArray[j + 1];

                cycles++;
                if(current > next) {
                    unsortedArray[j] = next;
                    unsortedArray[j + 1] = current;
                    isSorted = false;
                    swaps++;

                    int cyclesSnapshot = cycles;
                    int swapsSnapshot = swaps;
                    int[] arraySnapshot = unsortedArray.clone();
                    Platform.runLater(() -> {
                        chart.updateChart(arraySnapshot, new SortStats(cyclesSnapshot, swapsSnapshot));
                    });
                    try {
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if(isSorted) break;
        }
    }

    public static void selectionSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int cycles = 0;
        int swaps = 0;
        for (int i = 0; i < unsortedArray.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < unsortedArray.length; j++) {
                if(unsortedArray[j] < unsortedArray[minIndex]) {
                    minIndex = j;
                }

                cycles++;
            }

            int temp = unsortedArray[i];
            unsortedArray[i] = unsortedArray[minIndex];
            unsortedArray[minIndex] = temp;
            swaps++;

            int cyclesSnapshot = cycles;
            int swapsSnapshot = swaps;
            int[] arraySnapshot = unsortedArray.clone();
            Platform.runLater(() -> {
                chart.updateChart(arraySnapshot, new SortStats(cyclesSnapshot, swapsSnapshot));
            });
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void insertionSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int cycles = 0;
        int swaps = 0;

        // [ 0, 1, 2, 5, 8, 2]
        for (int i = 1; i < unsortedArray.length; i++) {
            int key = unsortedArray[i];
            int j = i - 1;

            while(j >= 0 && key < unsortedArray[j]) {
                unsortedArray[j + 1] = unsortedArray[j];
                j--;

                cycles++;
            }

            unsortedArray[j+1] = key;

            int cyclesSnapshot = cycles;
            int swapsSnapshot = swaps;
            int[] arraySnapshot = unsortedArray.clone();
            Platform.runLater(() -> {
                chart.updateChart(arraySnapshot, new SortStats(cyclesSnapshot, swapsSnapshot));
            });
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

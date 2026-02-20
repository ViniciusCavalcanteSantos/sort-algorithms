package org.viniciuscsantos.Helpers;

import javafx.application.Platform;
import org.viniciuscsantos.Views.ChartView;
import org.viniciuscsantos.Views.SortStats;

import java.util.Arrays;
import java.util.Timer;

public class SortAlgorithms {
    public static int[] bubbleSort(int[] array, ChartView chart) {
        int[] unsortedArray = array.clone();

        TimeManager timeManager = new TimeManager();
        timeManager.startTimer("teste");

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
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if(isSorted) break;
        }
        timeManager.finishTimer("teste");
        IO.println(timeManager.getTimePassed("teste"));

        return unsortedArray;
    }
}

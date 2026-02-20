package org.viniciuscsantos.Helpers;

import javafx.application.Platform;
import org.viniciuscsantos.Views.ChartView;

import java.util.Arrays;
import java.util.Timer;

public class SortAlgorithms {
    public static int[] bubbleSort(int[] array, ChartView chart) {
        int[] unsortedArray = array.clone();

        TimeManager timeManager = new TimeManager();
        timeManager.startTimer("teste");

        int attempts = 0;
        for (int i = 0; i < unsortedArray.length; i++) {
            boolean isSorted = true;

            for (int j = 0; j < unsortedArray.length - i - 1; j++) {
                int current = unsortedArray[j];
                int next = unsortedArray[j + 1];

                attempts++;
                if(current > next) {
                    unsortedArray[j] = next;
                    unsortedArray[j + 1] = current;
                    isSorted = false;

                    int finalAttempts = attempts;
                    Platform.runLater(() -> {
                        IO.println(Arrays.toString(unsortedArray));
                        chart.updateChart(unsortedArray, finalAttempts);
                    });
                    try {
                        Thread.sleep(100);
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

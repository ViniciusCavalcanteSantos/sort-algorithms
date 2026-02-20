package org.viniciuscsantos.Views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Helpers.SortAlgorithms;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

public class SortView {
    private VBox root;
    private Label labelCounter;
    private Button button;


    Thread workerThread;
    int counter = 0;

    public SortView() {
        root = new VBox(10);
        root.setStyle("-fx-background-color: blue;");

        // Elementos
        button = new Button("Iniciar");
        button.setStyle("-fx-background-color: aqua;");

        labelCounter = new Label("Contador: 0");

        button.setOnAction(actionEvent -> {
            toggleCounter();
        });

        root.getChildren().addAll(getBarChart(), button);

    }

    private void toggleCounter() {
        if(workerThread != null && workerThread.isAlive()) {
            workerThread.interrupt();
            return;
        }
        IO.println(root.getWidth());
        IO.println(root.getHeight());
        IO.println("Iniciando contador");
        button.setText("Pausar");
        workerThread = new Thread(() -> {
            try {
                for (int i = counter; i < 10000; i++) {
                    if(Thread.interrupted()) {
                        break;
                    }
                    counter = i;

                    Platform.runLater(() -> {
                        labelCounter.setText("contador: %d".formatted(counter));
                        button.setStyle("-fx-min-width: %dpx;".formatted(counter));

                    });
                    Thread.sleep(1000);

                }
            } catch (InterruptedException e) {
                IO.println("Pausado em " + counter);
            } finally {
                Platform.runLater(() -> {
                    button.setText("Iniciar");
                });
            }
        });
        workerThread.start();
    }

    public VBox getBarChart() {
        ChartView chart = new ChartView();

        int[] array = IntStream.rangeClosed(20, 40).toArray();
        IO.println(Arrays.toString(array));

        ArrayHelper.shuffle(array, true);

        chart.updateChart(array, 0);

        (new Thread(() -> {
            SortAlgorithms.bubbleSort(array, chart);
        })).start();

        return chart.getRoot();
    }

    public VBox getRoot() {
        return root;
    }
}

package org.viniciuscsantos.Views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SortView {
    private VBox root;
    private Label labelCounter;
    private Button button;


    Thread workerThread;
    int counter = 0;

    public SortView() {
        this.root = new VBox(10);

        // Elementos
        button = new Button("Iniciar");
        button.setStyle("-fx-background-color: aqua;");

        labelCounter = new Label("Contador: 0");

        button.setOnAction(actionEvent -> {
            toggleCounter();
        });

        root.getChildren().addAll(labelCounter, button);

    }

    private void toggleCounter() {
        if(workerThread != null && workerThread.isAlive()) {
            workerThread.interrupt();
            return;
        }

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

    public VBox getRoot() {
        return root;
    }
}

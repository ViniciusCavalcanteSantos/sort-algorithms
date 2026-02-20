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

    public SortView() {
        this.root = new VBox(10);

        Button button = new Button("Iniciar");
        button.setStyle("-fx-background-color: aqua;");


        Label l = new Label("Contador: 0");
        (new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                try {
                    int finalI = i;
                    Platform.runLater(() -> {
                        l.setText("contador: "+ finalI);
                    });
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        })).start();

        root.getChildren().addAll(l, button);

    }

    public VBox getRoot() {
        return root;
    }
}

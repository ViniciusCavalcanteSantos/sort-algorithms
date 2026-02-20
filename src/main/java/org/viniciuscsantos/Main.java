package org.viniciuscsantos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Helpers.TimeManager;
import org.viniciuscsantos.Views.SortView;

import java.util.stream.IntStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // CONFIGS
        int arraySize = 10;

        SortView mainScreen = new SortView();
        TimeManager timeManager = new TimeManager();


        int[] array = IntStream.rangeClosed(1, arraySize).toArray();

        timeManager.startTimer("shuffle");
        ArrayHelper.shuffle(array, true);
        timeManager.finishTimer("shuffle");

        IO.println(timeManager.getTimePassed("shuffle"));
        System.out.println(timeManager.getTimePassed("shuffle"));

        String javafxVersion = System.getProperty("javafx.version");

        stage.setTitle("Ordenador - JavaFX v" + javafxVersion);

        SortView view = new SortView();
        Scene scene = new Scene(view.getRoot(), 800, 600);
        scene.getStylesheets().add(
                getClass().getResource("/styles/base.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }

    void main() {
        launch();
    }
}

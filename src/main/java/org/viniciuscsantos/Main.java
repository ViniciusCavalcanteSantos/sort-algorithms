package org.viniciuscsantos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Helpers.TimeManager;
import org.viniciuscsantos.Views.SortView;

import java.util.stream.IntStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SortView mainScreen = new SortView();

        String javafxVersion = System.getProperty("javafx.version");
        stage.setTitle("Ordenador - JavaFX v" + javafxVersion);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainScreen.getRoot());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");
        Scene scene = new Scene(scrollPane, 1600, 800);
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

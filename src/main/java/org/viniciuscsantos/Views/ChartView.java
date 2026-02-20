package org.viniciuscsantos.Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.viniciuscsantos.Helpers.ArrayHelper;

public class ChartView {
    private HBox root;

    double defaultWidth = 300;
    double defaultHeight = 300;

    public ChartView() {
        root = new HBox(2);
        root.setPrefSize(defaultWidth, defaultHeight);
        root.setMinWidth(defaultWidth);
        root.setMaxWidth(defaultWidth);
        root.setAlignment(Pos.BOTTOM_CENTER);
        root.setPadding(new Insets(10, 5, 0, 5));
        root.setStyle("-fx-background-color: aqua;");
    }

    public void updateChart(int[] numbers) {
        root.getChildren().clear();

        double containerHeight = root.getHeight() == 0.0 ? defaultHeight : root.getHeight();
        int biggestNumber = ArrayHelper.getMax(numbers);

        for (int i = 0; i < numbers.length; i++) {
            int number = numbers[i];
            float percentage = (float) number / biggestNumber;
            double height = containerHeight * percentage;

            addBar(height);
        }
    }

    private void addBar(double height) {
        Region bar = new Region();
        bar.setPrefHeight(height);
        bar.setMinHeight(height);
        bar.setMaxHeight(height);

        bar.setStyle("-fx-background-color: red;");
        HBox.setHgrow(bar, Priority.ALWAYS);

        root.getChildren().add(bar);
    }

    public HBox getRoot() {
        return root;
    }
}

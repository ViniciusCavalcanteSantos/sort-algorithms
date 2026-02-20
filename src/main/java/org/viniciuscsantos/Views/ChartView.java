package org.viniciuscsantos.Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.viniciuscsantos.Helpers.ArrayHelper;

public class ChartView {
    private VBox root;

    private HBox chart;
    private Label infoLabel;

    double chartWidth = 300;
    double chartHeight = 300;

    public ChartView() {
        root = new VBox(5);
        root.setPadding(new Insets(10, 5, 0, 5));

        // Gráfico
        chart = new HBox(2);
        chart.setPrefSize(chartWidth, chartHeight);
        chart.setMinWidth(chartWidth);
        chart.setMaxWidth(chartWidth);
        chart.setAlignment(Pos.BOTTOM_CENTER);
        chart.setStyle("-fx-background-color: aqua;");
        chart.setPadding(new Insets(10, 5, 0, 5));

        // Label
        infoLabel = new Label("Tentativas: 0");
        infoLabel.setStyle("-fx-font-size: 30px;-fx-text-fill: red");

        root.getChildren().addAll(chart, infoLabel);
    }

    public void updateChart(int[] numbers, int attempts) {
        chart.getChildren().clear();

        infoLabel.setText("Tentativas: "+attempts);

        double containerHeight = chart.getHeight() == 0.0 ? chartHeight : chart.getHeight() - 10;
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

        chart.getChildren().add(bar);
    }

    public VBox getRoot() {
        return root;
    }
}

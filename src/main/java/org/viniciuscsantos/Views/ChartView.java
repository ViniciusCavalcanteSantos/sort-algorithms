package org.viniciuscsantos.Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Interfaces.IChartView;

public class ChartView implements IChartView {
    private VBox root;

    private HBox chart;
    private Label infoLabel;

    double chartWidth = 600;
    double chartHeight = 300;

    public ChartView() {
        root = new VBox(5);
        root.setPadding(new Insets(10, 5, 0, 5));

        // Gráfico
        chart = new HBox(0);
        chart.setPrefSize(chartWidth, chartHeight);
        chart.setMinWidth(chartWidth);
        chart.setMaxWidth(chartWidth);
        chart.setMinHeight(chartHeight);
        chart.setMaxHeight(chartHeight);
        chart.setAlignment(Pos.BOTTOM_CENTER);
        chart.setStyle("-fx-background-color: aqua;");
        chart.setPadding(new Insets(10, 5, 0, 5));

        // Label
        infoLabel = new Label("Ciclos: 0; Trocas: 0");
        infoLabel.setStyle("-fx-font-size: 30px;-fx-text-fill: red");

        root.getChildren().addAll(chart, infoLabel);
    }

    public void updateChart(int[] numbers, SortStats stats) {
        infoLabel.setText("Ciclos: %d; Trocas: %d".formatted(stats.getCycles(), stats.getSwaps()));

        double containerHeight = chartHeight - 10;
        int biggestNumber = ArrayHelper.getMax(numbers);

        if(chart.getChildren().isEmpty()) {
            for (int i = 0; i < numbers.length; i++) {
                int number = numbers[i];
                double percentage = (double) number / biggestNumber;
                double height = containerHeight * percentage;

                addBar(height);
            }
        } else {
            for (int i = 0; i < numbers.length; i++) {
                int number = numbers[i];
                double percentage = (double) number / biggestNumber;
                double height = containerHeight * percentage;

                Region bar = (Region) chart.getChildren().get(i);
                bar.setPrefHeight(height);
                bar.setMinHeight(height);
                bar.setMaxHeight(height);
            }
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

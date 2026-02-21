package org.viniciuscsantos.Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Interfaces.IChartView;

public class CanvasChartView implements IChartView {
    private VBox root;

    private Canvas chart;
    private GraphicsContext gc;

    private Label titleLabel;
    private Label infoLabel;

    double chartWidth = 300;
    double chartHeight = 300;

    double prefGap = 2;

    public CanvasChartView(String title) {
        root = new VBox(5);
        root.setPadding(new Insets(10, 5, 0, 5));

        // Gráfico
        chart = new Canvas(chartWidth, chartHeight);
        gc = chart.getGraphicsContext2D();

        gc.setFill(Color.AQUA);
        gc.fillRect(0, 0, chartWidth, chartHeight);

        // Label Title
        titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px;-fx-text-fill: red");

        // Label Info
        infoLabel = new Label("Comparações: 0;\nAtribuicões: 0");
        infoLabel.setStyle("-fx-font-size: 20px;-fx-text-fill: red");
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(300);

        root.getChildren().addAll(titleLabel, chart, infoLabel);
    }

    public void updateChart(int[] numbers, SortStats stats) {
        infoLabel.setText("Comparações: %d;\nAtribuicões: %d".formatted(stats.getComparisons(), stats.getAssignments()));

        int biggestNumber = ArrayHelper.getMax(numbers);
        int size = numbers.length;
        double barWidth = chartWidth / size;

        gc.setFill(Color.AQUA);
        gc.fillRect(0, 0, chartWidth, chartHeight);

        gc.setFill(Color.RED);

        for (int i = 0; i < numbers.length; i++) {
            int number = numbers[i];
            double percentage = (double) number / biggestNumber;
            double height = chartHeight * percentage;

            double x = i*barWidth;
            double y = chartHeight - height;

            gc.fillRect(x, y, barWidth + 1, height);
        }
    }

    public VBox getRoot() {
        return root;
    }
}

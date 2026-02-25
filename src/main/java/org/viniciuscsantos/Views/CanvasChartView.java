package org.viniciuscsantos.Views;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Helpers.TimeManager;
import org.viniciuscsantos.Interfaces.IChartView;

public class CanvasChartView implements IChartView {
    private VBox root;

    private Canvas chart;
    private GraphicsContext gc;

    private Label titleLabel;
    private Label infoLabel;
    private Label timerLabel;

    double chartWidth = 300;
    double chartHeight = 300;

    private TimeManager timeManager = new TimeManager();

    public CanvasChartView(String title) {
        root = new VBox(5);
        root.setPadding(new Insets(10, 5, 0, 5));
        root.getStyleClass().add("chart-container");

        // Gráfico
        chart = new Canvas(chartWidth, chartHeight);
        gc = chart.getGraphicsContext2D();

        gc.clearRect(0, 0, chartWidth, chartHeight);

        // Label Title
        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("chart-title");

        // Label Info
        infoLabel = new Label("Comparações: 0;\nAtribuicões: 0");
        infoLabel.getStyleClass().add("chart-stats");
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(300);

        // Timer
        timerLabel = new Label("Tempo: ");
        timerLabel.getStyleClass().add("chart-stats");

        root.getChildren().addAll(titleLabel, chart, infoLabel, timerLabel);
    }

    public void updateChart(SortStats stats) {
        infoLabel.setText("Comparações: %d;\nAtribuicões: %d".formatted(stats.getComparisons(), stats.getAssignments()));

        Long nanos = stats.getElapsedNanos();
        if(nanos != null) {
            timerLabel.setText("Tempo: " + timeManager.formatNanos(nanos));
        }

        int[] numbers = stats.getArray();
        int biggestNumber = ArrayHelper.getMax(numbers);
        int size = numbers.length;
        double barWidth = chartWidth / size;

        gc.clearRect(0, 0, chartWidth, chartHeight);

        gc.setFill(Color.web("#4ec9b0")); // Match CSS accent color

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

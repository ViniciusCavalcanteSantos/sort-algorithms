package org.viniciuscsantos.Interfaces;

import javafx.scene.layout.VBox;
import org.viniciuscsantos.Views.SortStats;

public interface IChartView {
    void updateChart(int[] numbers, SortStats stats);
    VBox getRoot();
}

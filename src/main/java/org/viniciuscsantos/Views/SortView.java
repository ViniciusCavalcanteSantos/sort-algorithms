package org.viniciuscsantos.Views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Helpers.SortAlgorithms;
import org.viniciuscsantos.Interfaces.IChartView;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

public class SortView {
    private VBox root;
    private HBox chartsContainer;

    private Button button;

    ChoiceBox cbRenderMethod;
    ChoiceBox cbGenerateMethod;

    TextField tfFrom;
    TextField tfTo;
    TextField tfAmount;

    int[] mainArray;
    IChartView chart;

    Thread mainThread;

    public SortView() {
        root = new VBox(10);
        root.setStyle("-fx-background-color: blue;");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(5);

        // Selecionar renderização
        cbRenderMethod = new ChoiceBox(FXCollections.observableArrayList(
                "Canvas", "VBox"
        ));
        cbRenderMethod.setValue("Canvas");
        cbRenderMethod.setTooltip(new Tooltip("Selecione uma opção"));

        // Selecionar tipo de geração números
        cbGenerateMethod = new ChoiceBox(FXCollections.observableArrayList(
                "Ordenado", "Aleatório"
        ));
        cbGenerateMethod.setValue("Ordenado");
        cbGenerateMethod.setTooltip(new Tooltip("Selecione uma opção"));


        // Inicio/Fim (ordenado)
        tfFrom = new TextField("0");
        tfTo = new TextField("200");

        // Quantidade (aleatório)
        tfAmount = new TextField("200");
        tfAmount.setVisible(false);
        tfAmount.setManaged(false);

        Button buttonGenerate = new Button("Gerar");
        buttonGenerate.setStyle("-fx-background-color: aqua;");

        // Elementos
        button = new Button("Iniciar");
        button.setStyle("-fx-background-color: aqua;");

        // Posicionamento do Grid
        GridPane.setConstraints(cbRenderMethod, 0, 0);
        GridPane.setConstraints(cbGenerateMethod, 1, 0);

        GridPane.setConstraints(tfFrom, 0, 1);
        GridPane.setConstraints(tfTo, 1, 1);
        GridPane.setConstraints(tfAmount, 0, 2);
        GridPane.setConstraints(buttonGenerate, 2, 0);
        GridPane.setConstraints(button, 3, 0);

        grid.getChildren().addAll(cbRenderMethod, cbGenerateMethod, tfFrom, tfTo, tfAmount, buttonGenerate, button);

        chartsContainer = new HBox(10);
        chartsContainer.getChildren().addAll(generateCharts());

        // Seletor de modo de geração dos números
        cbGenerateMethod.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue) {
                        if(newValue.intValue() == 0) {
                            tfAmount.setVisible(false);
                            tfAmount.setManaged(false);
                        } else {
                            tfAmount.setVisible(true);
                            tfAmount.setManaged(true);
                        }
                    }
                }
        );

        buttonGenerate.setOnAction(actionEvent -> {
            stopSort();

            chartsContainer.getChildren().clear();
            chartsContainer.getChildren().addAll(generateCharts());
        });

        button.setOnAction(actionEvent -> {
            startSort();
        });

        root.getChildren().addAll(
                grid,
                chartsContainer
        );
    }

    public VBox generateCharts() {
        String renderMethod = cbRenderMethod.getValue().toString();
        String generationMethod = cbGenerateMethod.getValue().toString();

        int from = Integer.parseInt(tfFrom.getText());
        int to = Integer.parseInt(tfTo.getText());
        int amount = Integer.parseInt(tfAmount.getText());

        if(Objects.equals(renderMethod, "Canvas")) {
            chart = new CanvasChartView();
        } else {
            chart = new ChartView();
        }

        if(Objects.equals(generationMethod, "Ordenado")) {
            mainArray = IntStream.rangeClosed(from, to).toArray();
            ArrayHelper.shuffle(mainArray, true);
        } else {
            Random random = new Random();

            mainArray = new int[amount];
            for (int i = 0; i < amount; i++) {
                mainArray[i] = random.nextInt(from, to);
            }
        }

        IO.println("Array gerado");
        IO.println(Arrays.toString(mainArray));
        chart.updateChart(mainArray, new SortStats(from, to));
        return chart.getRoot();
    }

    public void startSort() {
        mainThread = new Thread(() -> {
            SortAlgorithms.bubbleSort(mainArray, chart);
        });
        mainThread.start();
    }

    public void stopSort() {
        if (mainThread != null && mainThread.isAlive()) {
            mainThread.interrupt();
        }
    }

    public VBox getRoot() {
        return root;
    }
}

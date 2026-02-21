package org.viniciuscsantos.Views;

import javafx.animation.AnimationTimer;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class SortView {
    private VBox root;
    private HBox chartsContainer;

    private Button buttonGenerate;
    private Button buttonStart;

    ChoiceBox cbRenderMethod;
    ChoiceBox cbGenerateMethod;

    TextField tfFrom;
    TextField tfTo;
    TextField tfAmount;

    VBox tfAmounContainer;

    int[] mainArray;

    IChartView[] charts;
    Thread[] threads;

    private AnimationTimer renderLoop;

    public SortView() {
        root = new VBox(10);
        root.setStyle("-fx-background-color: blue;");
        chartsContainer = new HBox(10);

        initComponents();
        setupLayout();
        setupEventHandlers();

        generateCharts();
    }

    /**
     * Responsável por instanciar os controles visuais.
     */
    private void initComponents() {
        // Select Render
        cbRenderMethod = new ChoiceBox(FXCollections.observableArrayList("Canvas", "VBox"));
        cbRenderMethod.setValue("Canvas");
        cbRenderMethod.setTooltip(new Tooltip("Selecione uma opção"));

        // Select Generate
        cbGenerateMethod = new ChoiceBox(FXCollections.observableArrayList("Ordenado", "Aleatório"));
        cbGenerateMethod.setValue("Ordenado");
        cbGenerateMethod.setTooltip(new Tooltip("Selecione uma opção"));

        // Inputs
        tfFrom = new TextField("0");
        tfTo = new TextField("200");
        tfAmount = new TextField("200");
    }

    /**
     * Responsável por posicionar os elementos na tela.
     */
    private void setupLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(5);

        // Controls
        VBox renderContainer = createLabeledControl("Render:", cbRenderMethod);
        VBox generateContainer = createLabeledControl("Geração:", cbGenerateMethod);
        VBox tfFromContainer = createLabeledControl("De:", tfFrom);
        VBox tfToContainer = createLabeledControl("Até:", tfTo);

        tfAmounContainer = createLabeledControl("Quantidade:", tfAmount);
        tfAmounContainer.setVisible(false);
        tfAmounContainer.setManaged(false);

        // Buttons
        buttonGenerate = new Button("Gerar");
        buttonStart = new Button("Iniciar");
        buttonStart.setStyle("-fx-background-color: aqua;");

        // Posicionamento do Grid
        GridPane.setConstraints(renderContainer, 0, 0);
        GridPane.setConstraints(generateContainer, 1, 0);

        GridPane.setConstraints(tfFromContainer, 0, 1);
        GridPane.setConstraints(tfToContainer, 1, 1);
        GridPane.setConstraints(tfAmounContainer, 0, 2);
        GridPane.setConstraints(buttonGenerate, 2, 0);
        GridPane.setConstraints(buttonStart, 3, 0);

        grid.getChildren().addAll(renderContainer, generateContainer, tfFromContainer, tfToContainer, tfAmounContainer, buttonGenerate, buttonStart);
        root.getChildren().addAll(
                grid,
                chartsContainer
        );
    }

    /**
     * Responsável por atrelar todos os eventos (Listeners e Actions) aos botões e inputs.
     */
    private void setupEventHandlers() {
        // Seletor de modo de geração dos números
        cbGenerateMethod.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue) {
                        if(newValue.intValue() == 0) {
                            tfAmounContainer.setVisible(false);
                            tfAmounContainer.setManaged(false);
                        } else {
                            tfAmounContainer.setVisible(true);
                            tfAmounContainer.setManaged(true);
                        }
                    }
                }
        );

        buttonGenerate.setOnAction(actionEvent -> {
            stopSort();

            chartsContainer.getChildren().clear();
            generateCharts();
        });

        buttonStart.setOnAction(actionEvent -> {
            stopSort();
            startSort();
        });
    }

    private VBox createLabeledControl(String labelText, Control control) {
        VBox container = new VBox(4);
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: red;");
        container.getChildren().addAll(label, control);
        return container;
    }

    public void generateCharts() {
        String renderMethod = cbRenderMethod.getValue().toString();
        String generationMethod = cbGenerateMethod.getValue().toString();

        int from = Integer.parseInt(tfFrom.getText());
        int to = Integer.parseInt(tfTo.getText());
        int amount = Integer.parseInt(tfAmount.getText());

        charts = new IChartView[4];
        for (int i = 0; i < charts.length; i++) {
            String title;
            if(i == 0) {
                title = "Bubble Sort";
            } else if (i == 1) {
                title = "Selection Sort";
            } else if (i == 2){
                title = "Insertion Sort";
            } else if (i == 3) {
                title = "Shell Sort";
            } else {
                title = "";
            }

            if(Objects.equals(renderMethod, "Canvas")) {
                charts[i] = new CanvasChartView(title);
            } else {
                charts[i] = new ChartView(title);
            }
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

        for (int i = 0; i < charts.length; i++) {
            charts[i].updateChart(new SortStats(mainArray, from, to));
            chartsContainer.getChildren().add(charts[i].getRoot());
        }
    }

    public void startSort() {
        threads = new Thread[charts.length];
        threads[0] = new Thread(() -> {
            SortAlgorithms.bubbleSort(mainArray, charts[0]);
        });

        threads[1] = new Thread(() -> {
            SortAlgorithms.selectionSort(mainArray, charts[1]);
        });

        threads[2] = new Thread(() -> {
            SortAlgorithms.insertionSort(mainArray, charts[2]);
        });

        threads[3] = new Thread(() -> {
            SortAlgorithms.shellSort(mainArray, charts[3]);
        });

        startRenderLoop();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

    }

    public void stopSort() {
        if(threads == null || threads.length == 0) return;

        stopRenderLoop();
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null && threads[i].isAlive()) {
                threads[i].interrupt();
            }
        }
    }

    public void startRenderLoop() {
        renderLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (IChartView chart : charts) {
                    AtomicReference<SortStats> mailbox = SortAlgorithms.mailboxes.get(chart);
                    if (mailbox != null) {
                        SortStats stats = mailbox.getAndSet(null);
                        if (stats != null) {
                            chart.updateChart(new SortStats(stats.getArray(), stats.getComparisons(), stats.getAssignments()));
                        }
                    }
                }
            }
        };
        renderLoop.start();
    }

    public void stopRenderLoop() {
        if(renderLoop != null) {
            renderLoop.stop();
        }
    }

    public VBox getRoot() {
        return root;
    }
}

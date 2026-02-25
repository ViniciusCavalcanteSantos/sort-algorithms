package org.viniciuscsantos.Views;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.viniciuscsantos.Enums.Algorithms;
import org.viniciuscsantos.Helpers.ArrayHelper;
import org.viniciuscsantos.Helpers.SortAlgorithms;
import org.viniciuscsantos.Interfaces.IChartView;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class SortView {
    private final BorderPane root;
    private final FlowPane chartsContainer;

    private Button buttonGenerate;
    private Button buttonToggleAll;

    ChoiceBox<String> cbRenderMethod;
    ChoiceBox<String> cbGenerateMethod;

    TextField tfFrom;
    TextField tfTo;
    TextField tfAmount;

    TextField tfSpeedThrottle;

    VBox tfAmounContainer;

    int[] mainArray;

    IChartView[] charts;

    private AnimationTimer renderLoop;
    private final SortAlgorithms sortAlgorithms = new SortAlgorithms();

    public SortView() {
        root = new BorderPane();
        root.getStyleClass().add("main-view");
        
        chartsContainer = new FlowPane(20, 20);
        chartsContainer.setAlignment(Pos.CENTER);
        chartsContainer.getStyleClass().add("charts-container");
        chartsContainer.setPadding(new Insets(20));

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
        cbRenderMethod = new ChoiceBox<>(FXCollections.observableArrayList("Canvas", "VBox"));
        cbRenderMethod.setValue("Canvas");
        cbRenderMethod.setTooltip(new Tooltip("Selecione uma opção"));
        cbRenderMethod.getStyleClass().add("choice-box");
        cbRenderMethod.setMaxWidth(Double.MAX_VALUE);

        // Select Generate
        cbGenerateMethod = new ChoiceBox<>(FXCollections.observableArrayList("Ordenado", "Aleatório"));
        cbGenerateMethod.setValue("Ordenado");
        cbGenerateMethod.setTooltip(new Tooltip("Selecione uma opção"));
        cbGenerateMethod.getStyleClass().add("choice-box");
        cbGenerateMethod.setMaxWidth(Double.MAX_VALUE);

        // Inputs
        tfFrom = new TextField("0");
        tfTo = new TextField("200");
        tfAmount = new TextField("200");
        tfSpeedThrottle = new TextField("5");
    }

    /**
     * Responsável por posicionar os elementos na tela.
     */
    private void setupLayout() {
        // Sidebar
        VBox sidebar = new VBox(15);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);

        // Controls
        VBox renderContainer = createLabeledControl("Renderização:", cbRenderMethod);
        VBox generateContainer = createLabeledControl("Método de Geração:", cbGenerateMethod);
        
        Separator sep1 = new Separator();
        
        VBox tfFromContainer = createLabeledControl("De:", tfFrom);
        VBox tfToContainer = createLabeledControl("Até:", tfTo);
        tfAmounContainer = createLabeledControl("Quantidade:", tfAmount);
        tfAmounContainer.setVisible(false);
        tfAmounContainer.setManaged(false);

        Separator sep2 = new Separator();

        VBox tfSpeedThrottleContainer = createLabeledControl("Espera (ms):", tfSpeedThrottle);

        // Buttons
        buttonGenerate = new Button("Gerar Novos Dados");
        buttonGenerate.getStyleClass().add("button");
        buttonGenerate.setMaxWidth(Double.MAX_VALUE);
        
        buttonToggleAll = new Button("Iniciar Ordenação");
        buttonToggleAll.getStyleClass().addAll("button", "primary");
        buttonToggleAll.setMaxWidth(Double.MAX_VALUE);

        VBox buttonsContainer = new VBox(10, buttonGenerate, buttonToggleAll);

        sidebar.getChildren().addAll(
            createSectionLabel("Configuração"),
            renderContainer, 
            generateContainer,
            sep1,
            createSectionLabel("Dados"),
            tfFromContainer, 
            tfToContainer, 
            tfAmounContainer,
            sep2,
            createSectionLabel("Controle"),
            tfSpeedThrottleContainer,
            new Region(), // Spacer
            buttonsContainer
        );

        // Center Area (Charts)
        ScrollPane scrollPane = new ScrollPane(chartsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.setLeft(sidebar);
        root.setCenter(scrollPane);
    }

    /**
     * Responsável por atrelar todos os eventos (Listeners e Actions) aos botões e inputs.
     */
    private void setupEventHandlers() {
        // Seletor de modo de geração dos números
        cbGenerateMethod.getSelectionModel().selectedIndexProperty().addListener(
                (_, _, newValue) -> {
                    if(newValue.intValue() == 0) {
                        tfAmounContainer.setVisible(false);
                        tfAmounContainer.setManaged(false);
                    } else {
                        tfAmounContainer.setVisible(true);
                        tfAmounContainer.setManaged(true);
                    }
                }
        );

        buttonGenerate.setOnAction(_ -> {
            stopSort();

            chartsContainer.getChildren().clear();
            generateCharts();
        });

        buttonToggleAll.setOnAction(_ -> {
            boolean atLeastOneRunning = false;
            boolean atLeastOnePaused = false;
            for (IChartView chart : charts) {
                if(!atLeastOneRunning && sortAlgorithms.isRunning(chart)) atLeastOneRunning = true;
                if(!atLeastOnePaused && sortAlgorithms.isPaused(chart)) atLeastOnePaused = true;
            }


            if(atLeastOneRunning && !atLeastOnePaused) {
                pauseSort();
                return;
            }

            if(atLeastOneRunning && atLeastOnePaused) {
                resumeSort();
                return;
            }

            stopSort();
            startSort();
        });
    }

    private VBox createLabeledControl(String labelText, Control control) {
        VBox container = new VBox(5);
        Label label = new Label(labelText);
        label.getStyleClass().add("info-label");
        container.getChildren().addAll(label, control);
        return container;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("sidebar-label");
        return label;
    }

    public void generateCharts() {
        String renderMethod = cbRenderMethod.getValue();
        String generationMethod = cbGenerateMethod.getValue();

        int from = stringToInt(tfFrom.getText(), 0);
        int to = stringToInt(tfTo.getText(), 200);
        int amount = stringToInt(tfAmount.getText(), 100);

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

        System.out.println("Array gerado");
        System.out.println(Arrays.toString(mainArray));

        for (IChartView chart : charts) {
            chart.updateChart(new SortStats(mainArray, from, to, 0L));
            chartsContainer.getChildren().add(chart.getRoot());
        }
    }

    public void startSort() {
        startRenderLoop();

        int speedThrottle = stringToInt(tfSpeedThrottle.getText(), 5);
        sortAlgorithms.setSleepMillis(speedThrottle);
        sortAlgorithms.start(Algorithms.BUBBLE_SORT, mainArray, charts[0]);
        sortAlgorithms.start(Algorithms.SELECTION_SORT, mainArray, charts[1]);
        sortAlgorithms.start(Algorithms.INSERTION_SORT, mainArray, charts[2]);
        sortAlgorithms.start(Algorithms.SHELL_SORT, mainArray, charts[3]);

        buttonToggleAll.setText("Pausar");
        buttonToggleAll.getStyleClass().remove("primary");
        buttonToggleAll.getStyleClass().add("accent");
    }

    public void stopSort() {
        sortAlgorithms.pauseAll();
        stopRenderLoop();

        buttonToggleAll.setText("Iniciar Ordenação");
        buttonToggleAll.getStyleClass().remove("accent");
        buttonToggleAll.getStyleClass().add("primary");
    }

    public void resumeSort() {
        int speedThrottle = stringToInt(tfSpeedThrottle.getText(), 5);
        sortAlgorithms.setSleepMillis(speedThrottle);
        sortAlgorithms.resumeAll();
        buttonToggleAll.setText("Pausar");
        buttonToggleAll.getStyleClass().remove("primary");
        buttonToggleAll.getStyleClass().add("accent");
    }

    public void pauseSort() {
        sortAlgorithms.pauseAll();
        buttonToggleAll.setText("Retomar");
        buttonToggleAll.getStyleClass().remove("accent");
        buttonToggleAll.getStyleClass().add("primary");
    }

    public void startRenderLoop() {
        renderLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (IChartView chart : charts) {
                    AtomicReference<SortStats> mailbox = sortAlgorithms.mailboxes.get(chart);
                    if (mailbox != null) {
                        SortStats stats = mailbox.getAndSet(null);
                        if (stats != null) {
                            chart.updateChart(new SortStats(stats.getArray(), stats.getComparisons(), stats.getAssignments(), stats.getElapsedNanos(), stats.getMarkers()));
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

    public Parent getRoot() {
        return root;
    }

    private int stringToInt(String text, int fallback) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return fallback;
        }
    }
}

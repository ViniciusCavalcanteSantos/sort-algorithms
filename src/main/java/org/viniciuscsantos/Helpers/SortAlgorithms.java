package org.viniciuscsantos.Helpers;

import org.viniciuscsantos.Enums.Algorithms;
import org.viniciuscsantos.Interfaces.IChartView;
import org.viniciuscsantos.Views.SortStats;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class SortAlgorithms {
    private int sleepMillis = 1;

    private final TimeManager timeManager = new TimeManager();

    private final ConcurrentHashMap<IChartView, Thread> activeThreads = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<IChartView, Boolean> runningStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<IChartView, Boolean> pausedStates = new ConcurrentHashMap<>();

    // Screen FPS Control
    public final ConcurrentHashMap<IChartView, AtomicReference<SortStats>> mailboxes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<IChartView, Long> lastUpdateTimes = new ConcurrentHashMap<>();
    private static final int FRAME_RATE_MS = 16;

    // Exceptions
    private static class SortStoppedException extends RuntimeException {}


    public void startAlgorithm(Algorithms algorithm, int[] array, IChartView chart) {
        if(runningStates.getOrDefault(chart, false)) return;
        runningStates.put(chart, true);
        pausedStates.put(chart, false);

        String timerkey = chart.toString();
        timeManager.startTimer(timerkey);
        Thread thread = new Thread(() -> {
           try {
               switch (algorithm) {
                   case BUBBLE_SORT -> bubbleSort(array, chart);
                   case SELECTION_SORT -> selectionSort(array, chart);
                   case INSERTION_SORT -> insertionSort(array, chart);
                   case SHELL_SORT -> shellSort(array, chart);
                   default -> throw new RuntimeException("Algorítimo não suportado: " + algorithm);
               }
           } catch (SortStoppedException e) {

           } finally {
               runningStates.put(chart, false);
           }
        });

        activeThreads.put(chart, thread);
        thread.start();
    }

    public void stopAlgorithm(IChartView chart) {
        runningStates.put(chart, false);
        Thread thread = activeThreads.get(chart);
        if(thread != null) {
            thread.interrupt();
            activeThreads.remove(chart);
        }
    }

    public void stopAll() {
        activeThreads.values().forEach(Thread::interrupt);
        activeThreads.clear();
        mailboxes.clear();
        lastUpdateTimes.clear();
        timeManager.clearAllTimers();
        runningStates.clear();
        pausedStates.clear();
    }

    public void pauseAlgorithm(IChartView chart) {
        if(runningStates.getOrDefault(chart, false)) {
            pausedStates.put(chart, true);
        }
    }

    public void resumeAlgorithm(IChartView chart) {
        if(runningStates.getOrDefault(chart, false)) {
            pausedStates.put(chart, false);
        }
    }

    public void pauseAll() {
        timeManager.pauseAllTimers();
        pausedStates.replaceAll((chart, state) -> true);
    }

    public void resumeAll() {
        timeManager.resumeAllTimers();
        pausedStates.replaceAll((chart, state) -> false);
    }

    public boolean isRunning(IChartView chart) {
        return runningStates.getOrDefault(chart, false);
    }

    public boolean isPaused(IChartView chart) {
        return pausedStates.getOrDefault(chart, false);
    }

    public void setSleepMillis(int sleepMillis) {
        this.sleepMillis = sleepMillis;
    }

    /**
     * Implementa o algoritmo Bubble Sort.
     * <p>
     * O Bubble Sort percorre repetidamente a lista, compara elementos adjacentes e os troca se estiverem na ordem errada.
     * Este processo é repetido até que a lista esteja ordenada.
     * </p>
     * <p>
     * Complexidade de Tempo: O(n²) no pior e médio caso; O(n) no melhor caso (se a lista já estiver ordenada).
     * <br>
     * Complexidade de Espaço: O(1) auxiliar.
     * </p>
     *
     * @param array O array de inteiros a ser ordenado.
     * @param chart A interface para atualização visual do gráfico.
     */
    private void bubbleSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int comparisons = 0;
        int assignments = 0;
        for (int i = 0; i < unsortedArray.length; i++) {
            boolean isSorted = true;

            for (int j = 0; j < unsortedArray.length - i - 1; j++) {
                comparisons++;
                if(unsortedArray[j] > unsortedArray[j + 1]) {
                    int temp = unsortedArray[j];
                    unsortedArray[j] = unsortedArray[j + 1];
                    unsortedArray[j + 1] = temp;
                    assignments += 2;

                    isSorted = false;

                    updateChart(unsortedArray, comparisons, assignments, chart);
                }
            }

            if(isSorted) break;
        }

        forceUpdateChart(unsortedArray, comparisons, assignments, chart);
    }

    /**
     * Implementa o algoritmo Selection Sort.
     * <p>
     * O Selection Sort divide a lista em duas partes: a sublista de itens já ordenados e a sublista de itens restantes.
     * O algoritmo busca repetidamente o menor elemento da sublista não ordenada e o troca com o elemento mais à esquerda da parte não ordenada.
     * </p>
     * <p>
     * Complexidade de Tempo: O(n²) em todos os casos, pois sempre percorre o restante do array.
     * <br>
     * Complexidade de Espaço: O(1) auxiliar.
     * </p>
     *
     * @param array O array de inteiros a ser ordenado.
     * @param chart A interface para atualização visual do gráfico.
     */
    private void selectionSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int comparisons = 0;
        int assignments = 0;
        for (int i = 0; i < unsortedArray.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < unsortedArray.length; j++) {
                comparisons++;
                if(unsortedArray[j] < unsortedArray[minIndex]) {
                    minIndex = j;
                }

            }

            int temp = unsortedArray[i];
            unsortedArray[i] = unsortedArray[minIndex];
            unsortedArray[minIndex] = temp;
            assignments += 2;

            updateChart(unsortedArray, comparisons, assignments, chart);
        }

        forceUpdateChart(unsortedArray, comparisons, assignments, chart);
    }

    /**
     * Implementa o algoritmo Insertion Sort.
     * <p>
     * O Insertion Sort constrói o array ordenado um item de cada vez.
     * Ele itera sobre os elementos de entrada e os insere na posição correta dentro da parte já ordenada do array.
     * É eficiente para pequenos conjuntos de dados ou arrays quase ordenados.
     * </p>
     * <p>
     * Complexidade de Tempo: O(n²) no pior e médio caso; O(n) no melhor caso.
     * <br>
     * Complexidade de Espaço: O(1) auxiliar.
     * </p>
     *
     * @param array O array de inteiros a ser ordenado.
     * @param chart A interface para atualização visual do gráfico.
     */
    private void insertionSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int comparisons = 0;
        int assignments = 0;

        // [ 0, 1, 2, 5, 8, 2]
        for (int i = 1; i < unsortedArray.length; i++) {
            int key = unsortedArray[i];
            int j = i - 1;

            while(j >= 0) {
                comparisons++;
                if (key < unsortedArray[j]) {
                    unsortedArray[j + 1] = unsortedArray[j];
                    assignments++;
                    j--;
                } else {
                    break;
                }
            }

            unsortedArray[j+1] = key; assignments++;

            updateChart(unsortedArray, comparisons, assignments, chart);
        }

        forceUpdateChart(unsortedArray, comparisons, assignments, chart);
    }

    /**
     * Implementa o algoritmo Shell Sort.
     * <p>
     * O Shell Sort é uma generalização do Insertion Sort que permite a troca de itens distantes.
     * A ideia é organizar a lista de elementos de modo que, começando de qualquer lugar, pegando cada h-ésimo elemento,
     * produza uma lista ordenada.
     * </p>
     * <p>
     * Complexidade de Tempo: Depende da sequência de lacunas (gaps). No pior caso, pode ser O(n²), mas com sequências otimizadas pode chegar a O(n log n).
     * <br>
     * Complexidade de Espaço: O(1) auxiliar.
     * </p>
     *
     * @param array O array de inteiros a ser ordenado.
     * @param chart A interface para atualização visual do gráfico.
     */
    private void shellSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int comparisons = 0;
        int assignments = 0;

        int size = unsortedArray.length;
        int gap = (int) Math.floor((double) size / 2);
        while(gap > 0) {
            for (int i = gap; i < size; i++) {
                int temp = unsortedArray[i];
                int j = i;

                comparisons++;
                while (j >= gap) {
                    comparisons++;
                    if (unsortedArray[j-gap] > temp) {
                        unsortedArray[j] = unsortedArray[j-gap];
                        assignments++;
                        j -= gap;
                    } else {
                        break;
                    }

                }
                unsortedArray[j] = temp;  assignments++;

                updateChart(unsortedArray, comparisons, assignments, chart);
            }
            gap = (int) Math.floor((double) gap / 2);
        }

        forceUpdateChart(unsortedArray, comparisons, assignments, chart);
    }

    /**
     * Atualiza o gráfico visual com o estado atual do array e as estatísticas de ordenação.
     * <p>
     * Este método cria snapshots dos dados para garantir que a atualização da UI ocorra de forma consistente,
     * mesmo que o algoritmo de ordenação continue executando em outra thread.
     * Também introduz um pequeno atraso (sleep) para permitir a visualização do processo.
     * </p>
     *
     * @param array O estado atual do array sendo ordenado.
     * @param comparisons O número atual de ciclos comparações.
     * @param assignments O número atual de atribuições realizadas.
     * @param chart A interface do gráfico a ser atualizada.
     */
    private void updateChart(int[] array, int comparisons, int assignments, IChartView chart) {
        handleThreadState(chart);

        String timerkey = chart.toString();
        long elapsedNanos = timeManager.getElapsedNanos(timerkey);

        mailboxes.putIfAbsent(chart, new AtomicReference<>(null));
        lastUpdateTimes.putIfAbsent(chart, 0L);

        long now = System.currentTimeMillis();
        long lastTime = lastUpdateTimes.get(chart);

        if(now - lastTime >= FRAME_RATE_MS) {
            mailboxes.get(chart).set(new SortStats(array.clone(), comparisons, assignments, elapsedNanos));
            lastUpdateTimes.put(chart, now);
        }

        if(sleepMillis > 0) {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void forceUpdateChart(int[] array, int comparisons, int assignments, IChartView chart) {
        if(mailboxes.containsKey(chart)) {
            mailboxes.get(chart).set(new SortStats(array.clone(), comparisons, assignments, null));
        }
    }

    /**
     * Trava a thread se estiver pausada e lança exceção se for parada.
     */
    private void handleThreadState(IChartView chart) {
        if (!isRunning(chart)) {
            throw new SortStoppedException();
        }
        while (isPaused(chart) && isRunning(chart)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SortStoppedException();
            }
        }
    }
}
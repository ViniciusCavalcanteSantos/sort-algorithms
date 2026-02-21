package org.viniciuscsantos.Helpers;

import javafx.application.Platform;
import org.viniciuscsantos.Interfaces.IChartView;
import org.viniciuscsantos.Views.SortStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortAlgorithms {
    static int sleepMillis = 10;

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
    public static void bubbleSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int cycles = 0;
        int swaps = 0;
        for (int i = 0; i < unsortedArray.length; i++) {
            boolean isSorted = true;

            for (int j = 0; j < unsortedArray.length - i - 1; j++) {
                int current = unsortedArray[j];
                int next = unsortedArray[j + 1];

                cycles++;
                if(current > next) {
                    unsortedArray[j] = next;
                    unsortedArray[j + 1] = current;
                    isSorted = false;
                    swaps++;

                    updateChart(unsortedArray, cycles, swaps, chart);
                }
            }

            if(isSorted) break;
        }
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
    public static void selectionSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int cycles = 0;
        int swaps = 0;
        for (int i = 0; i < unsortedArray.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < unsortedArray.length; j++) {
                if(unsortedArray[j] < unsortedArray[minIndex]) {
                    minIndex = j;
                }

                cycles++;
            }

            int temp = unsortedArray[i];
            unsortedArray[i] = unsortedArray[minIndex];
            unsortedArray[minIndex] = temp;
            swaps++;

            updateChart(unsortedArray, cycles, swaps, chart);
        }
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
    public static void insertionSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int cycles = 0;
        int swaps = 0;

        // [ 0, 1, 2, 5, 8, 2]
        for (int i = 1; i < unsortedArray.length; i++) {
            int key = unsortedArray[i];
            int j = i - 1;

            while(j >= 0 && key < unsortedArray[j]) {
                unsortedArray[j + 1] = unsortedArray[j];
                j--;

                cycles++;
            }

            unsortedArray[j+1] = key;

            updateChart(unsortedArray, cycles, swaps, chart);
        }
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
    public static void shellSort(int[] array, IChartView chart) {
        int[] unsortedArray = array.clone();

        int cycles = 0;
        int swaps = 0;

        int size = unsortedArray.length;
        int gap = (int) Math.floor((double) size / 2);
        while(gap > 0) {
            for (int i = gap; i < size; i++) {
                int temp = unsortedArray[i];
                int j = i;

                while (j >= gap && unsortedArray[j-gap] > temp) {
                    unsortedArray[j] = unsortedArray[j-gap];
                    j -= gap;
                }
                unsortedArray[j] = temp;

                updateChart(unsortedArray, cycles, swaps, chart);
            }
            gap = (int) Math.floor((double) gap / 2);
        }
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
     * @param cycles O número atual de ciclos (comparações/iterações) realizados.
     * @param swaps O número atual de trocas realizadas.
     * @param chart A interface do gráfico a ser atualizada.
     */
    private static void updateChart(int[] array, int cycles, int swaps, IChartView chart) {
        int cyclesSnapshot = cycles;
        int swapsSnapshot = swaps;
        int[] arraySnapshot = array.clone();
        Platform.runLater(() -> {
            chart.updateChart(arraySnapshot, new SortStats(cyclesSnapshot, swapsSnapshot));
        });
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
package org.viniciuscsantos.Helpers;

import java.util.Random;

public class ArrayHelper {
    public static void shuffle(int[] array, boolean useXORSwap) {
        Random rand = new Random();
        for (int i = 0; i < array.length - 1; i++) {
            int index = rand.nextInt(i+1);


            if (useXORSwap) {
                // XOR Swap
                if (index != i) {
                    array[index] ^= array[i];
                    array[i] ^= array[index];
                    array[index] ^= array[i];
                }
            } else {
                // Normal Swap
                int temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
        }
    }
}

package ua.yuriih.task2;

import ua.yuriih.task2.linalg.TridiagonalMatrix;
import ua.yuriih.task2.linalg.TridiagonalMatrixSystem;

import java.util.Arrays;
import java.util.Random;

public class Main {
    private static TridiagonalMatrixSystem getRandomSolvableSystem(int size, double scale) {
        double[] c = new double[size];
        double[] a = new double[size - 1];
        double[] b = new double[size - 1];
        double[] d = new double[size];

        Random rng = new Random();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                a[i - 1] = rng.nextDouble() * scale;
                c[i] += a[i - 1];
            }
            if (i != size - 1) {
                b[i] = rng.nextDouble() * scale;
                c[i] += b[i];
            }
            c[i] += rng.nextDouble() * scale;
            d[i] = rng.nextDouble() * scale;
        }
        return new TridiagonalMatrixSystem(new TridiagonalMatrix(a, b, c), d);
    }
    
    public static void main(String[] args) {
        System.out.println(Arrays.toString(getRandomSolvableSystem(100, 5).solve()));
    }
}

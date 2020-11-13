package ua.yuriih.task2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yuriih.task2.linalg.TridiagonalMatrix;
import ua.yuriih.task2.linalg.TridiagonalMatrixSystem;

import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
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
        TridiagonalMatrixSystem system = getRandomSolvableSystem(99, 5);
//        double[] a = new double[] { 5, -5, 4, 1 };
//        double[] b = new double[] { -3, 2, 0, -1 };
//        double[] c = new double[] { 5, 8, 11, 13, -16 };
//        double[] d = new double[] { 3, -3, 5, -5, 1 };
//        TridiagonalMatrixSystem system = new TridiagonalMatrixSystem(new TridiagonalMatrix(a, b, c), d);
//        LOGGER.info(system.toString());
        double[] x = system.solve();
//        LOGGER.info(Arrays.toString(x));

        LOGGER.info("Is solution? {}", system.isSolution(x));
    }
}

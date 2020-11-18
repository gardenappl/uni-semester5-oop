package ua.yuriih.task2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yuriih.task2.linalg.TridiagonalMatrix;
import ua.yuriih.task2.linalg.TridiagonalMatrixSystem;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    
    public static void main(String[] args) {
        double[] a = new double[] { 5, -5, 4, 1 };
        double[] b = new double[] { -3, 2, 0, -1 };
        double[] c = new double[] { 5, 8, 11, 13, -16 };
        double[] d = new double[] { 3, -3, 5, -5, 1 };
        TridiagonalMatrixSystem system = new TridiagonalMatrixSystem(new TridiagonalMatrix(a, b, c), d);
        LOGGER.info(system.toString());

        double[] x = system.solve();
        LOGGER.info(Arrays.toString(x));
        LOGGER.info("Is solution? {}", system.isSolution(x));

        x = system.solve(false);
        LOGGER.info(Arrays.toString(x));
        LOGGER.info("Is solution? {}", system.isSolution(x));


        system = TridiagonalMatrixSystem.getRandomSolvableSystem(2, 5);
        x = system.solve();
        LOGGER.info("Is solution? {}", system.isSolution(x));
        x = system.solve(false);
        LOGGER.info("Is solution? {}", system.isSolution(x));


        TridiagonalMatrixSystem[] testSystems = new TridiagonalMatrixSystem[100];
        for (int i = 0; i < testSystems.length; i++)
            testSystems[i] = TridiagonalMatrixSystem.getRandomSolvableSystem(99999, 5);


        runBenchmark(testSystems, "Non-parallel solution",
                (TridiagonalMatrixSystem testSystem) ->
                        testSystem.solve(false));

        runBenchmark(testSystems, "Parallel solution",
                (TridiagonalMatrixSystem testSystem) ->
                        testSystem.solve(true));
    }
    
    private static void runBenchmark(TridiagonalMatrixSystem[] testSystems,
                                     String testName,
                                     Consumer<TridiagonalMatrixSystem> test)
    {
        long nanoStart = System.nanoTime();
        for (TridiagonalMatrixSystem testSystem : testSystems) {
            test.accept(testSystem);
        }
        long nanoEnd = System.nanoTime();
        LOGGER.info("{}: {} ms", testName, (nanoEnd - nanoStart) / 1_000_000);
    }
}

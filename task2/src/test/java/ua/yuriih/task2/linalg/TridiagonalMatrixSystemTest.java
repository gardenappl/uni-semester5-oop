package ua.yuriih.task2.linalg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TridiagonalMatrixSystemTest {
    @Test
    void create_fail() {
        TridiagonalMatrix matrix = new TridiagonalMatrix(new double[5],
                new double[5], new double[6]);

        assertThrows(IllegalArgumentException.class, () ->
                new TridiagonalMatrixSystem(matrix, new double[7]));
    }

    @Test
    void getRandomSolvableSystem() {
        TridiagonalMatrixSystem system = TridiagonalMatrixSystem.getRandomSolvableSystem(100, 50);
        TridiagonalMatrix matrix = system.getMatrix();

        //Must be diagonally dominant by row
        for (int i = 0; i < matrix.getSize(); i++) {
            double othersSum = 0;
            if (i != 0)
                othersSum += Math.abs(matrix.getA(i - 1));
            if (i != matrix.getSize() - 1)
                othersSum += Math.abs(matrix.getB(i));

            assert(Math.abs(matrix.getC(i)) >= othersSum);
        }
    }

    @Test
    void isSolution() {
        double[] a = new double[] { 5, -5, 4, 1 };
        double[] b = new double[] { -3, 2, 0, -1 };
        double[] c = new double[] { 5, 8, 11, 13, -16 };
        double[] d = new double[] { 3, -3, 5, -5, 1 };
        TridiagonalMatrixSystem system = new TridiagonalMatrixSystem(new TridiagonalMatrix(a, b, c), d);
        
        double[] x = new double[] { 0.25191, -0.58015, 0.19084, -0.45031, -0.09064 };
        
        assert(system.isSolution(x, 0.001));
        assert(system.isSolution(x));

        assertFalse(system.isSolution(x, 0.0000001d));
        x[0] = 1;
        assertFalse(system.isSolution(x));
        assert(system.isSolution(x, 10));
    }

    @Test
    public void solve_parallel() {
        solve_doTest(true);
    }

    @Test
    public void solve_non_parallel() {
        solve_doTest(false);
    }

    void solve_doTest(boolean parallel) {
        for (int i = 0; i < 1000; i++) {
            TridiagonalMatrixSystem system = TridiagonalMatrixSystem.getRandomSolvableSystem(10 + i, 10 * i);
            assert(system.isSolution(system.solve(parallel)));
        }
    }
}
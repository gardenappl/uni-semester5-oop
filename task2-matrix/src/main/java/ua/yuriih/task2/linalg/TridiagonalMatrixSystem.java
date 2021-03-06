package ua.yuriih.task2.linalg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yuriih.task2.linalg.TridiagonalMatrix;

import java.util.Arrays;
import java.util.Random;

public class TridiagonalMatrixSystem {
    private final TridiagonalMatrix matrix;
    private final double[] d;
    
    public TridiagonalMatrixSystem(TridiagonalMatrix matrix, double[] d) {
        if (d.length != matrix.getSize())
            throw new IllegalArgumentException("Array D must have same length as size of the matrix");
        this.matrix = matrix;
        this.d = d;
    }

    //Creates system with diagonally dominant matrix
    public static TridiagonalMatrixSystem getRandomSolvableSystem(int size) {
        return getRandomSolvableSystem(size, 1.d);
    }
    
    public static TridiagonalMatrixSystem getRandomSolvableSystem(int size, double scale) {
        double[] c = new double[size];
        double[] a = new double[size - 1];
        double[] b = new double[size - 1];
        double[] d = new double[size];

        Random rng = new Random();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                a[i - 1] = (rng.nextDouble() - 0.5) * scale;
                c[i] += Math.abs(a[i - 1]);
            }
            if (i != size - 1) {
                b[i] = (rng.nextDouble() - 0.5) * scale;
                c[i] += Math.abs(b[i]);
            }
            c[i] += Math.abs((rng.nextDouble() - 0.5) * scale);
            if (rng.nextBoolean())
                c[i] *= -1;
            d[i] = (rng.nextDouble() - 0.5) * scale;
        }
        return new TridiagonalMatrixSystem(new TridiagonalMatrix(a, b, c), d);
    }

    public TridiagonalMatrix getMatrix() {
        return matrix;
    }

    public double getD(int index) {
        return d[index];
    }

    @Override
    public String toString() {
        return matrix.toString() + ", d = " + Arrays.toString(d);
    }
    
    public boolean isSolution(double[] x) {
        return isSolution(x, 0.0001);
    }

    public boolean isSolution(double[] x, double epsilon) {
        if (x.length != matrix.getSize())
            throw new IllegalArgumentException("Array X must have same length as size of matrix");
        
        for (int i = 0; i < matrix.getSize(); i++) {
            double result = 0;
            if (i != 0) {
                result += matrix.getA(i - 1) * x[i - 1];
            }
            result += matrix.getC(i) * x[i];
            if (i != matrix.getSize() - 1) {
                result += matrix.getB(i) * x[i + 1];
            }
            /*System.err.println("r[" + i + "]: " + result);
            System.err.println("d[" + i + "]: " + d[i]);*/
            if (Math.abs(result - getD(i)) > epsilon)
                return false;
        }
        return true;
    }

    public double[] solve() {
        return solve(true);
    }

    public double[] solve(boolean parallel) {
        if (parallel)
            return new ParallelSolver(this).solve();

        //Standard Thomas algorithm

        int n = matrix.getSize();
        double[] bModified = new double[n - 1];
        double[] dModified = new double[n - 1];

        bModified[0] = matrix.getB(0) / matrix.getC(0);
        dModified[0] = getD(0) / matrix.getC(0);
        for (int i = 1; i < n - 1; i++) {
            double divisor = matrix.getC(i) - matrix.getA(i - 1) * bModified[i - 1];
            bModified[i] = matrix.getB(i) / divisor;
            dModified[i] = (getD(i) - matrix.getA(i - 1) * dModified[i - 1]) / divisor;
        }
        double[] result = new double[n];

        result[n - 1] = (getD(n - 1) - matrix.getA(n - 2) * dModified[n - 2]) /
                (matrix.getC(n - 1) - matrix.getA(n - 2) * bModified[n - 2]);

        for (int i = n - 2; i >= 0; i--) {
            result[i] = dModified[i] - bModified[i] * result[i + 1];
        }
        return result;
    }
    
    
    private static class ParallelSolver {
        private static final Logger LOGGER = LoggerFactory.getLogger(ParallelSolver.class);
        
        private final TridiagonalMatrix matrix;
        private final double[] f;

        private final int mid;
        private final int n;

        private final double[] upAlpha;
        private final double[] upBeta;
        private final double[] downXi;
        private final double[] downEta;

        public ParallelSolver(TridiagonalMatrixSystem system) {
            this.matrix = system.matrix;
            this.f = system.d;
            n = matrix.getSize();
            mid = n / 2;
            //Upper: [0 .. mid]
            this.upAlpha = new double[mid + 1];
            this.upBeta = new double[mid + 1];
            //Lower: [mid .. n-1] -> [0 .. n-1-mid]
            this.downXi = new double[n - mid];
            this.downEta = new double[n - mid];
        }
        
        double[] solve() {
            // Forward run

            Thread upperThread = new Thread(() -> {
                upAlpha[1] = -matrix.getB(0) / matrix.getC(0);
                upBeta[1] = f[0] / matrix.getC(0);

                for (int i = 1; i < mid; i++) {
                    double divisor = matrix.getC(i) + matrix.getA(i - 1) * upAlpha[i];
                    upAlpha[i + 1] = -matrix.getB(i) / divisor;
                    upBeta[i + 1] = (f[i] - matrix.getA(i - 1) * upBeta[i]) / divisor;
                }
            });

            upperThread.start();

            //Do lower half calculations in this thread

            downXi[n - 1 - mid] = -matrix.getA(n - 2) / matrix.getC(n - 1);
            downEta[n - 1 - mid] = f[n - 1] / matrix.getC(n - 1);

            for (int i = n - 2; i >= mid; i--) {
                double divisor = matrix.getC(i) + matrix.getB(i) * downXi[i - mid + 1];
                downXi[i - mid] = -matrix.getA(i - 1) / divisor;
                downEta[i - mid] = (f[i] - matrix.getB(i) * downEta[i - mid + 1]) / divisor;
            }

            try {
                upperThread.join();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted", e);
            }

            //Get elements in middle

            double[] result = new double[n];
            result[mid] = (downEta[0] + downXi[0] * upBeta[mid]) / (1 - downXi[0] * upAlpha[mid]);
            result[mid - 1] = (upBeta[mid] + upAlpha[mid] * downEta[0]) / (1 - downXi[0] * upAlpha[mid]);

            //Backwards run

            upperThread = new Thread(() -> {
                for (int i = mid - 2; i >= 0; i--) {
                    result[i] = upAlpha[i + 1] * result[i + 1] + upBeta[i + 1];
                }
            });

            upperThread.start();

            //lower half
            for (int i = mid + 1; i < n; i++) {
                result[i] = downXi[i - mid] * result[i - 1] + downEta[i - mid];
            }

            try {
                upperThread.join();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted", e);
            }
            
            return result;
        }
    }
}

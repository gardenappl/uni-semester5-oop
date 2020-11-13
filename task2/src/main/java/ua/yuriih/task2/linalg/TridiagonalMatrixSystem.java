package ua.yuriih.task2.linalg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yuriih.task2.linalg.TridiagonalMatrix;

import java.util.Arrays;

public class TridiagonalMatrixSystem {
    final TridiagonalMatrix matrix;
    final double[] d;
    
    public TridiagonalMatrixSystem(TridiagonalMatrix matrix, double[] d) {
        if (d.length != matrix.getSize())
            throw new IllegalArgumentException("Array D must have same length as size of the matrix");
        this.matrix = matrix;
        this.d = d;
    }

    public TridiagonalMatrix getMatrix() {
        return matrix;
    }
    
    @Override
    public String toString() {
        return matrix.toString() + ", d = " + Arrays.toString(d);
    }
    
    public double[] solve() {
        return new ParallelSolver(this).solve();
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
                result += matrix.a[i - 1] * x[i - 1];
            }
            result += matrix.c[i] * x[i];
            if (i != matrix.getSize() - 1) {
                result += matrix.b[i] * x[i + 1];
            }
            /*System.err.println("r[" + i + "]: " + result);
            System.err.println("d[" + i + "]: " + d[i]);*/
            if (Math.abs(result - d[i]) > epsilon)
                return false;
        }
        return true;
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
            Thread upperThread = new Thread(() -> {
                upAlpha[1] = -matrix.b[0] / matrix.c[0];
                upBeta[1] = f[0] / matrix.c[0];

                for (int i = 1; i < mid; i++) {
                    double divisor = matrix.c[i] + matrix.a[i - 1] * upAlpha[i];
                    upAlpha[i + 1] = -matrix.b[i] / divisor;
                    upBeta[i + 1] = (f[i] - matrix.a[i - 1] * upBeta[i]) / divisor;
                }
            });

            Thread lowerThread = new Thread(() -> {
                downXi[n - 1 - mid] = -matrix.a[n - 2] / matrix.c[n - 1];
                downEta[n - 1 - mid] = f[n - 1] / matrix.c[n - 1];

                for (int i = n - 2; i >= mid; i--) {
                    double divisor = matrix.c[i] + matrix.b[i] * downXi[i - mid + 1];
                    downXi[i - mid] = -matrix.a[i - 1] / divisor;
                    downEta[i - mid] = (f[i] - matrix.b[i] * downEta[i - mid + 1]) / divisor;
                }
            });

            lowerThread.start();
            upperThread.start();

            try {
                lowerThread.join();
                upperThread.join();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted", e);
            }

            double[] result = new double[n];
            result[mid] = (downEta[0] + downXi[0] * upBeta[mid]) / (1 - downXi[0] * upAlpha[mid]);
            result[mid - 1] = (upBeta[mid] + upAlpha[mid] * downEta[0]) / (1 - downXi[0] * upAlpha[mid]);

            upperThread = new Thread(() -> {
                for (int i = mid - 2; i >= 0; i--) {
                    result[i] = upAlpha[i + 1] * result[i + 1] + upBeta[i + 1];
                }
            });
            
            lowerThread = new Thread(() -> {
                for (int i = mid + 1; i < n; i++) {
                    result[i] = downXi[i - mid] * result[i - 1] + downEta[i - mid];
                }
            });

            upperThread.start();
            lowerThread.start();

            try {
                upperThread.join();
                lowerThread.join();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted", e);
            }
            
            return result;
        }
    }
}

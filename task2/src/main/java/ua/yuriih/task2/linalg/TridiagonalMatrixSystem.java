package ua.yuriih.task2.linalg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yuriih.task2.linalg.TridiagonalMatrix;

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
    
    public double[] solve() {
        return new ParallelSolver(this).solve();
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
            //Upper: 0 .. mid-1
            this.upAlpha = new double[mid];
            this.upBeta = new double[mid];
            //Lower: mid-1 .. n-1 -> 0 .. n-mid
            this.downXi = new double[n - mid + 1];
            this.downEta = new double[n - mid + 1];
        }
        
        double[] solve() {
            Thread upperThread = new Thread(() -> {
                upAlpha[0] = -matrix.b[0] / matrix.c[0];
                upBeta[0] = f[0] / matrix.c[0];

                for (int i = 1; i <= mid - 1; i++) {
                    double divisor = matrix.c[i] + matrix.a[i - 1] * upAlpha[i - 1];
                    upAlpha[i] = -matrix.b[i] / divisor;
                    upBeta[i] = (f[i] - matrix.a[i - 1] * upBeta[i - 1]) / divisor;
                }
            });

            Thread lowerThread = new Thread(() -> {
                downXi[n - mid] = -matrix.a[n - 2] / matrix.c[n - 1];
                downEta[n - mid] = f[n - 1] / matrix.c[n - 1];

                for (int i = n - 2; i >= mid - 1; i--) {
                    double divisor = matrix.c[i] + matrix.b[i] * downXi[i - (mid - 1) + 1];
                    downXi[i - (mid - 1)] = -matrix.a[i - 1] / divisor;
                    downEta[i - (mid - 1)] = (f[i] - matrix.b[i] * downEta[i - (mid - 1) + 1]) / divisor;
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
            result[mid - 1] = (downEta[0] + downXi[0] * upBeta[0]) / (1 - downXi[0] * upAlpha[mid - 1]);
            result[mid - 2] = (upBeta[mid - 1] + upAlpha[mid - 1] * downEta[0]) / (1 - downXi[0] * upAlpha[mid - 1]);

            upperThread = new Thread(() -> {
                for (int i = mid - 3; i >= 0; i--) {
                    result[i] = upAlpha[i + 1] * result[i + 1] + upBeta[i + 1];
                }
            });
            
            lowerThread = new Thread(() -> {
                for (int i = mid; i < n; i++) {
                    result[i] = downXi[i - (mid - 1)] * result[i - 1] + downEta[i - (mid - 1)];
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

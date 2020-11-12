package ua.yuriih.task2.linalg;

public class TridiagonalMatrix {
    final double[] a;
    final double[] b;
    final double[] c;

    public TridiagonalMatrix(double[] a, double[] b, double[] c) {
        if (a.length != c.length - 1)
            throw new IllegalArgumentException("Array A must have same length as C, minus 1");
        if (b.length != c.length - 1)
            throw new IllegalArgumentException("Array B must have same length as C, minus 1");
        if (c.length < 2)
            throw new IllegalArgumentException("Array C must have at least 2 elements");
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public double getA(int index) {
        return a[index];
    }

    public double getB(int index) {
        return b[index];
    }

    public double getC(int index) {
        return c[index];
    }
    
    public int getSize() {
        return c.length;
    }
}

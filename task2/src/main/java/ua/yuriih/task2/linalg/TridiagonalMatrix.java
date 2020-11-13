package ua.yuriih.task2.linalg;

public class TridiagonalMatrix {
    private final double[] a;
    private final double[] b;
    private final double[] c;

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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < getSize(); i++) {
            sb.append('[');
            for (int j = 0; j < getSize(); j++) {
                if (j == i - 1)
                    sb.append(a[i - 1]);
                else if (j == i + 1)
                    sb.append(b[i]);
                else if (j == i)
                    sb.append(c[i]);
                else
                    sb.append(0.d);
                if (j != getSize() - 1)
                    sb.append(", ");
            }
            sb.append("]\n");
        }
        sb.append(']');
        return sb.toString();
    }
}

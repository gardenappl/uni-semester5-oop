package ua.yuriih.task2.linalg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TridiagonalMatrixTest {

    @Test
    public void toString_test() {
        TridiagonalMatrix matrix = new TridiagonalMatrix(
                new double[] { -1, -2, -3, -4, -5 },
                new double[] { 0, 1, 2, 3, 4 },
                new double[] { 10, 11, 12, 13, 14, 15 }
        );

        assertEquals("[\n" +
                "[10.0, 0.0, 0.0, 0.0, 0.0, 0.0]\n" +
                "[-1.0, 11.0, 1.0, 0.0, 0.0, 0.0]\n" +
                "[0.0, -2.0, 12.0, 2.0, 0.0, 0.0]\n" +
                "[0.0, 0.0, -3.0, 13.0, 3.0, 0.0]\n" +
                "[0.0, 0.0, 0.0, -4.0, 14.0, 4.0]\n" +
                "[0.0, 0.0, 0.0, 0.0, -5.0, 15.0]\n" +
                "]", matrix.toString());
    }

    @Test
    public void create() {
        assertDoesNotThrow(() ->
                new TridiagonalMatrix(new double[5], new double[5], new double[6]));
    }

    @Test
    public void create_failMismatchSize() {
        assertThrows(IllegalArgumentException.class, () ->
                new TridiagonalMatrix(new double[5], new double[5], new double[4]));
        assertThrows(IllegalArgumentException.class, () ->
                new TridiagonalMatrix(new double[5], new double[7], new double[6]));
        assertThrows(IllegalArgumentException.class, () ->
                new TridiagonalMatrix(new double[4], new double[5], new double[6]));
        assertThrows(IllegalArgumentException.class, () ->
                new TridiagonalMatrix(new double[10], new double[10], new double[10]));
    }

    @Test
    public void create_failTooSmall() {
        assertThrows(IllegalArgumentException.class, () ->
                new TridiagonalMatrix(new double[0], new double[0], new double[1]));
    }
}
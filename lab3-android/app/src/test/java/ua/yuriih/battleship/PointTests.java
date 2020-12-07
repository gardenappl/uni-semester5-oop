package ua.yuriih.battleship;

import org.junit.Test;

import static org.junit.Assert.*;

import ua.yuriih.battleship.model.Point;

public class PointTests {
    @Test
    public void toString_test() {
        Point point = new Point(0, 1);
        assertEquals("A2", point.toString());

        point = new Point(6, 5);
        assertEquals("G6", point.toString());

        point = new Point(-4, 0);
        assertEquals("(-3, 1)", point.toString());

        point = new Point(55, 5);
        assertEquals("(56, 6)", point.toString());
    }

    @Test
    public void equals() {
        assertTrue(new Point(1, 5).equals(new Point(1, 5)));
        assertFalse(new Point(1, 5).equals(new Point(2, 5)));

        assertTrue(new Point(1, 5).hashCode() == new Point(1, 5).hashCode());
    }
}

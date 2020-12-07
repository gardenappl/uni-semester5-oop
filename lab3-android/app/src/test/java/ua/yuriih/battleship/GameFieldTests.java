package ua.yuriih.battleship;

import org.junit.Test;

import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;

import static org.junit.Assert.*;

public class GameFieldTests {
    @Test
    public void constructor() {
        boolean threwException = true;
        try {
            new GameField(5, 0);
            threwException = false;
        } catch (IllegalArgumentException ignored) {}
        assertTrue(threwException);

        GameField field2 = new GameField(15, 20);
        assertEquals(15, field2.getWidth());
        assertEquals(20, field2.getHeight());
    }

    @Test
    public void getCell() {
        GameField field = new GameField(10, 10);

        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                assertEquals(CellState.EMPTY, field.getCell(x, y));
            }
        }

        field.setCell(0, 0, CellState.EMPTY);
        field.setCell(1, 0, CellState.SHIP);
        field.setCell(2, 0, CellState.HIT);
        field.setCell(3, 0, CellState.MISSED);

        assertEquals(CellState.EMPTY, field.getCell(0, 0));
        assertEquals(CellState.SHIP, field.getCell(1, 0));
        assertEquals(CellState.HIT, field.getCell(2, 0));
        assertEquals(CellState.MISSED, field.getCell(3, 0));

        assertEquals(CellState.EMPTY, field.getCellAsOpponent(0, 0));
        assertEquals(CellState.EMPTY, field.getCellAsOpponent(1, 0));
        assertEquals(CellState.HIT, field.getCellAsOpponent(2, 0));
        assertEquals(CellState.MISSED, field.getCellAsOpponent(3, 0));
    }
}

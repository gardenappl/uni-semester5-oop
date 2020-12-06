package ua.yuriih.battleship.model;

import java.util.Arrays;

import ua.yuriih.battleship.model.CellState;

public class GameField {
    private final CellState[][] cells;

    public GameField(int width, int height) {
        cells = new CellState[width][height];
        for (CellState[] row : cells)
            Arrays.fill(row, CellState.EMPTY);

        cells[1][2] = CellState.HIT;
    }

    public CellState getCell(int x, int y) {
        return cells[y][x];
    }

    public void setCell(int x, int y, CellState state) {
        cells[y][x] = state;
    }
}

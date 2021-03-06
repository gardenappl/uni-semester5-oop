package ua.yuriih.battleship.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ua.yuriih.battleship.model.CellState;

public class GameField {
    private final CellState[][] cells;
    private final int width;
    private final int height;

    public GameField(int width, int height) {
        if (width < 10 || height < 10)
            throw new IllegalArgumentException("Width and height must be at least 10");
        cells = new CellState[width][height];
        for (CellState[] row : cells)
            Arrays.fill(row, CellState.EMPTY);

        this.width = width;
        this.height = height;
    }

    public CellState getCell(int x, int y) {
        return cells[y][x];
    }

    public CellState getCell(Point point) {
        return cells[point.y][point.x];
    }

    public CellState getCellAsOpponent(int x, int y) {
        CellState cell = getCell(x, y);
        if (cell == CellState.SHIP)
            return CellState.EMPTY;
        else
            return cell;
    }

    public CellState getCellAsOpponent(Point point) {
        return getCellAsOpponent(point.x, point.y);
    }

    public void setCell(int x, int y, CellState state) {
        cells[y][x] = state;
    }

    public void setCell(Point point, CellState state) {
        cells[point.y][point.x] = state;
    }

    public boolean isValidCellForShip(int x, int y, Set<Point> ignorePoints) {
        for (int neighborX = Math.max(0, x - 1); neighborX <= Math.min(width - 1, x + 1); neighborX++) {
            for (int neighborY = Math.max(0, y - 1); neighborY <= Math.min(height - 1, y + 1); neighborY++) {
                if (ignorePoints.contains(new Point(neighborX, neighborY)))
                    continue;
                if (getCell(neighborX, neighborY) != CellState.EMPTY)
                    return false;
            }
        }
        return true;
    }

    public boolean isValidCellForShip(int x, int y) {
        return isValidCellForShip(x, y, Collections.<Point>emptySet());
    }

    public boolean isValidCellForShip(Point point, Set<Point> ignorePoints) {
        return isValidCellForShip(point.x, point.y, ignorePoints);
    }

    public boolean isValidCellForShip(Point point) {
        return isValidCellForShip(point.x, point.y, Collections.<Point>emptySet());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameField gameField = (GameField) o;
        return Arrays.deepEquals(cells, gameField.cells);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cells);
    }
}

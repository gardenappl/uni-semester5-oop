package ua.yuriih.battleship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Point;

public final class TestUtils {
    public static GameField createFieldFromArray(int width, int height, int[][] ships) {
        GameField result = new GameField(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (ships[y][x] == 1)
                    result.setCell(x, y, CellState.SHIP);
                if (ships[y][x] == 2)
                    result.setCell(x, y, CellState.HIT);
                if (ships[y][x] == 3)
                    result.setCell(x, y, CellState.MISSED);
            }
        }
        return result;
    }
}

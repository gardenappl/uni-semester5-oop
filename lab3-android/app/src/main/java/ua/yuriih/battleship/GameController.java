package ua.yuriih.battleship;

import android.content.Context;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ua.yuriih.battleship.gamestates.GameState;
import ua.yuriih.battleship.gamestates.StateCreatePlayerField;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class GameController {

    private final Context appContext;

    private static final int SIZE = 10;
    private final GameField playerField;
    private final GameField aiField;

    private GameState state;

    private final ArrayList<View> views = new ArrayList<>();


    public GameController(Context appContext) {
        this.appContext = appContext;
        playerField = new GameField(SIZE, SIZE);
        aiField = new GameField(SIZE, SIZE);
        state = new StateCreatePlayerField(this);
    }

    public void registerView(View view) {
        views.add(view);
    }

    public int getWidth() {
        return SIZE;
    }

    public int getHeight() {
        return SIZE;
    }

    public Context getApplicationContext() {
        return appContext;
    }

    public GameField getField(Player player) {
        switch (player) {
            case HUMAN:
                return playerField;
            case AI:
                return aiField;
            default:
                throw new IllegalArgumentException();
        }
    }

/*    private boolean isValidCoordinate(int x, int y) {
        if (x == NOT_DRAWING) {
            if (y == NOT_DRAWING)
                return false;
            throw new IllegalArgumentException("x is invalid but y is valid");
        }
        if (y == NOT_DRAWING)
            throw new IllegalArgumentException("x is valid but y is invalid");
        return true;
    }

    public boolean isAdjacent(int x1, int y1, int x2, int y2) {
        if (!isValidCoordinate(x1, y1) || !isValidCoordinate(x2, y2))
            return true;
        return Math.abs(x2 - x1) <= 1 && Math.abs(y2 - y1) <= 1;
    }

    public boolean isTouching(int x1, int y1, int x2, int y2) {
        if (!isValidCoordinate(x1, y1) || !isValidCoordinate(x2, y2))
            return true;
        return (x1 == x2 && Math.abs(y2 - y1) == 1) ||
                (y1 == y2 && Math.abs(x2 - x1) == 1);
    }*/

    public void redrawUI() {
        for (View view : views)
            view.invalidate();
    }

    public void onTouchCellDown(Player player, int x, int y, int pointerId) {
        state.onTouchCellDown(player, x, y, pointerId);
    }

    public void onTouchCell(Player player, int x, int y, int pointerId) {
        state.onTouchCell(player, x, y, pointerId);
    }

    public void onTouchCellUp(Player player, int x, int y, int pointerId) {
        state.onTouchCellUp(player, x, y, pointerId);
    }
}

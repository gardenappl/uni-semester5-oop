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

import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class GameController {
    private enum State {
        CREATING_PLAYER_FIELD,
        PLAYING,
        GAME_ENDED
    }

    private static final String LOGGING_TAG = "GameController";

    private final Context appContext;

    private static final int SIZE = 10;
    private final GameField playerField;
    private final GameField aiField;

    private State state;

    private static final int NOT_DRAWING = Integer.MIN_VALUE;
    private int currentDrawingPointerId = NOT_DRAWING;
    private final Set<Point> currentDrawnFigure = new HashSet<>(4);
    private final Map<Integer, Integer> figureSizeCount = new HashMap<>(4);

    private final ArrayList<View> views = new ArrayList<>();
    private final int[] maxCountForSize = new int[] {0, 4, 3, 2, 1};


    public GameController(Context appContext) {
        this.appContext = appContext;
        playerField = new GameField(SIZE, SIZE);
        aiField = new GameField(SIZE, SIZE);
        state = State.CREATING_PLAYER_FIELD;
        for (int i = 1; i <= 4; i++)
            figureSizeCount.put(i, 0);
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

    private void onFailDrawPlayerCell(int x, int y) {
        Log.d(LOGGING_TAG, "invalid");
        Vibrator vibrator = (Vibrator)appContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    public int getMaxCountForFigureSize(int size) {
        if (size < 1)
            throw new IllegalArgumentException();
        if (size > 4)
            return 0;
        return maxCountForSize[size];
    }

    public int getCountForFigureSize(int size) {
        if (size < 1)
            throw new IllegalArgumentException();
        if (size > 4)
            return 0;
        return figureSizeCount.get(size);
    }

    private boolean drawPlayerCell(int x, int y) {
        Point point = new Point(x, y);
        if (currentDrawnFigure.contains(point))
            return false;

        int size = currentDrawnFigure.size();
        if (getCountForFigureSize(size + 1) < getMaxCountForFigureSize(size + 1) &&
                playerField.isValidCellForShip(x, y, currentDrawnFigure)) {
            Log.d(LOGGING_TAG, "valid");
            playerField.setCell(x, y, CellState.SHIP);
            currentDrawnFigure.add(point);

            for (View view : views)
                view.invalidate();
            return true;
        } else {
            onFailDrawPlayerCell(x, y);
            return false;
        }
    }

    public void onTouchCellDown(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN) {
            if (state == State.CREATING_PLAYER_FIELD && currentDrawingPointerId == NOT_DRAWING) {
                if (playerField.isValidCellForShip(x, y))
                    currentDrawingPointerId = pointerId;
                else
                    onFailDrawPlayerCell(x, y);
            }
        }
    }

    public void onTouchCell(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN) {
            if (state == State.CREATING_PLAYER_FIELD && currentDrawingPointerId == pointerId) {
                drawPlayerCell(x, y);
            }
        }
    }

    public void onTouchCellUp(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN) {
            if (state == State.CREATING_PLAYER_FIELD && currentDrawingPointerId == pointerId) {
                drawPlayerCell(x, y);
                currentDrawingPointerId = NOT_DRAWING;

                int figureSize = currentDrawnFigure.size();
                if (figureSize > 0) {
                    figureSizeCount.put(figureSize, figureSizeCount.get(figureSize) + 1);
                    currentDrawnFigure.clear();
                }
            }
        }
    }
}

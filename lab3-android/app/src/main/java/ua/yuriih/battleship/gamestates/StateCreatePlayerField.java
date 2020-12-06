package ua.yuriih.battleship.gamestates;

import android.content.Context;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ua.yuriih.battleship.GameController;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class StateCreatePlayerField extends GameState {
    private static final String LOGGING_TAG = "CreatePlayerField";

    private static final int NOT_DRAWING = Integer.MIN_VALUE;
    private int currentDrawingPointerId = NOT_DRAWING;

    private final Set<Point> currentDrawnFigure = new HashSet<>(4);
    private final Map<Integer, Integer> figureSizeCount = new HashMap<>(4);
    private final int[] maxCountForSize = new int[] {0, 4, 3, 2, 1};

    public StateCreatePlayerField(GameController controller) {
        super(controller);

        for (int i = 1; i <= 4; i++)
            figureSizeCount.put(i, 0);
    }

    private int getCountForFigureSize(int size) {
        if (size < 1)
            throw new IllegalArgumentException();
        if (size > 4)
            return 0;
        return figureSizeCount.get(size);
    }

    public int getMaxCountForFigureSize(int size) {
        if (size < 1)
            throw new IllegalArgumentException();
        if (size > 4)
            return 0;
        return maxCountForSize[size];
    }

    private boolean drawPlayerCell(int x, int y) {
        Point point = new Point(x, y);
        if (currentDrawnFigure.contains(point))
            return false;

        GameField humanField = getController().getField(Player.HUMAN);
        int size = currentDrawnFigure.size();
        if (getCountForFigureSize(size + 1) < getMaxCountForFigureSize(size + 1) &&
                humanField.isValidCellForShip(x, y, currentDrawnFigure)) {
            humanField.setCell(x, y, CellState.SHIP);
            currentDrawnFigure.add(point);

            getController().redrawUI();
            return true;
        } else {
            onFailDrawPlayerCell(x, y);
            return false;
        }
    }

    private void onFailDrawPlayerCell(int x, int y) {
        Context appContext = getController().getApplicationContext();
        Vibrator vibrator = (Vibrator)appContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    @Override
    public void onTouchCellDown(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN && currentDrawingPointerId == NOT_DRAWING) {
            GameField humanField = getController().getField(Player.HUMAN);
            if (humanField.isValidCellForShip(x, y))
                currentDrawingPointerId = pointerId;
            else
                onFailDrawPlayerCell(x, y);
        }
    }

    @Override
    public void onTouchCell(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN && currentDrawingPointerId == pointerId) {
            drawPlayerCell(x, y);
        }
    }

    @Override
    public void onTouchCellUp(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN && currentDrawingPointerId == pointerId) {
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

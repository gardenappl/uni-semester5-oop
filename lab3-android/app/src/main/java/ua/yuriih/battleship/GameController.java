package ua.yuriih.battleship;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import ua.yuriih.battleship.gamestates.GameState;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class GameController {
    private static final String LOGGING_TAG = "GameController";

    private final Context appContext;
    private GameActivity gameActivity;
    private final Random rng;

    private static final int SIZE = 10;
    private final GameField humanField;
    private final GameField aiField;
    private final int[] maxCountForSize = new int[] {0, 4, 3, 2, 1};

    private GameState nextState;
    private GameState state;

    private final ArrayList<View> views = new ArrayList<>();


    public GameController(Context appContext) {
        this.appContext = appContext;
        humanField = new GameField(SIZE, SIZE);
        aiField = new GameField(SIZE, SIZE);
        rng = new Random();
    }

    public Random getRNG() {
        return rng;
    }

    public void registerView(View view) {
        views.add(view);
    }
    
    public void setGameActivity(GameActivity activity) {
        this.gameActivity = activity;
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
                return humanField;
            case AI:
                return aiField;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getMaxShipCount(int size) {
        if (size < 1)
            throw new IllegalArgumentException();
        if (size > 4)
            return 0;
        return maxCountForSize[size];
    }

    public boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) <= 1 && Math.abs(y2 - y1) <= 1;
    }

    public boolean isTouching(int x1, int y1, int x2, int y2) {
        return (x1 == x2 && Math.abs(y2 - y1) == 1) ||
                (y1 == y2 && Math.abs(x2 - x1) == 1);
    }

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

    public void onTouchCellUp(Player player, int pointerId) {
        state.onTouchCellUp(player, pointerId);
    }

    public void setNextState(GameState nextState) {
        this.nextState = nextState;
    }

    public void startNextState() {
        GameState oldState = this.state;
        GameState newState = this.nextState;

        this.state = this.nextState;
        this.nextState = null;
        this.state.start();

        gameActivity.onStateChange(oldState, newState);
    }

    public void vibrateForFailure() {
        Vibrator vibrator = (Vibrator)appContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    public boolean checkShipDestroyed(Player player, int x, int y) {
        GameField field = getField(player);
        boolean[][] visited = new boolean[getHeight()][getWidth()];
        if (Boolean.TRUE.equals(checkShipDestroyedRecursive(field, x, y, visited))) {
            visited = new boolean[getHeight()][getWidth()];
            markDestroyedShip(field, x, y, false, visited);
            return true;
        }
        return false;
    }

    private Boolean checkShipDestroyedRecursive(GameField field, int x, int y, boolean[][] visited) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
            return null;
        if (visited[y][x])
            return null;

        visited[y][x] = true;
        CellState cell = field.getCell(x, y);
        if (cell == CellState.EMPTY || cell == CellState.MISSED)
            return null;

        if (cell == CellState.SHIP)
            return false;

        if (Boolean.FALSE.equals(checkShipDestroyedRecursive(field, x - 1, y, visited)))
            return false;
        if (Boolean.FALSE.equals(checkShipDestroyedRecursive(field, x + 1, y, visited)))
            return false;
        if (Boolean.FALSE.equals(checkShipDestroyedRecursive(field, x, y - 1, visited)))
            return false;
        if (Boolean.FALSE.equals(checkShipDestroyedRecursive(field, x, y + 1, visited)))
            return false;

        return true;
    }

    private void markDestroyedShip(GameField field, int x, int y, boolean justMark, boolean[][] visited) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
            return;
        if (visited[y][x])
            return;

        CellState cell = field.getCell(x, y);
        if (cell == CellState.EMPTY) {
            visited[y][x] = true;
            field.setCell(x, y, CellState.MISSED);
            Log.d(LOGGING_TAG, "Marked " + x + " " + y + " as missed");
        }

        if (cell == CellState.HIT && !justMark) {
            visited[y][x] = true;
            Log.d(LOGGING_TAG, "Marking more starting from " + x + " " + y);
            markDestroyedShip(field, x - 1, y, false, visited);
            markDestroyedShip(field, x + 1, y, false, visited);
            markDestroyedShip(field, x, y - 1, false, visited);
            markDestroyedShip(field, x, y + 1, false, visited);

            markDestroyedShip(field, x - 1, y - 1, true, visited);
            markDestroyedShip(field, x + 1, y - 1, true, visited);
            markDestroyedShip(field, x + 1, y + 1, true, visited);
            markDestroyedShip(field, x - 1, y + 1, true, visited);
        }
    }

    public boolean checkVictory(Player player) {
        GameField opponentField;
        if (player == Player.AI)
            opponentField = humanField;
        else
            opponentField = aiField;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (opponentField.getCell(x, y) == CellState.SHIP)
                    return false;
            }
        }
        return true;
    }
}

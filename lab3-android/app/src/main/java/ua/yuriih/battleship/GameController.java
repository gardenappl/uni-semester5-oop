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
import java.util.Random;
import java.util.Set;

import ua.yuriih.battleship.gamestates.GameState;
import ua.yuriih.battleship.gamestates.StateCreatePlayerField;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class GameController {
    private final Context appContext;
    private GameActivity gameActivity;
    private final Random rng;

    private static final int SIZE = 10;
    private final GameField playerField;
    private final GameField aiField;
    private final int[] maxCountForSize = new int[] {0, 4, 3, 2, 1};

    private GameState nextState;
    private GameState state;

    private final ArrayList<View> views = new ArrayList<>();


    public GameController(Context appContext) {
        this.appContext = appContext;
        playerField = new GameField(SIZE, SIZE);
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
                return playerField;
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
}

package ua.yuriih.battleship;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

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

    private static final int SIZE = 10;
    private GameField playerField;
    private GameField aiField;

    private State state;

    private static final int NOT_DRAWING = -1;
    private int currentDrawingPointerId = NOT_DRAWING;

    private ArrayList<View> views = new ArrayList<>();


    public GameController() {
        playerField = new GameField(SIZE, SIZE);
        aiField = new GameField(SIZE, SIZE);
        state = State.CREATING_PLAYER_FIELD;
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

    private GameField getField(Player player) {
        switch (player) {
            case HUMAN:
                return playerField;
            case AI:
                return aiField;
            default:
                throw new IllegalArgumentException();
        }
    }

    public CellState getCell(Player player, int x, int y) {
        return getField(player).getCell(x, y);
    }

    public CellState getCellAsOpponent(Player player, int x, int y) {
        CellState cell = getField(player).getCell(x, y);
        if (cell == CellState.SHIP)
            return CellState.EMPTY;
        else
            return CellState.SHIP;
    }

    public void onTouchCellDown(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN) {
            if (state == State.CREATING_PLAYER_FIELD && currentDrawingPointerId == NOT_DRAWING) {
                currentDrawingPointerId = pointerId;
            }
        }
    }

    public void onTouchCell(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN) {
            if (state == State.CREATING_PLAYER_FIELD && currentDrawingPointerId == pointerId) {
                for (View view : views)
                    view.invalidate();
                playerField.setCell(x, y, CellState.SHIP);
            }
        }
    }

    public void onTouchCellUp(Player player, int x, int y, int pointerId) {
        if (player == Player.HUMAN) {
            if (state == State.CREATING_PLAYER_FIELD && currentDrawingPointerId == pointerId) {
                currentDrawingPointerId = NOT_DRAWING;
            }
        }
    }
}

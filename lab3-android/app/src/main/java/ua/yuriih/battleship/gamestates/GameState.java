package ua.yuriih.battleship.gamestates;

import ua.yuriih.battleship.GameController;
import ua.yuriih.battleship.model.Player;

public abstract class GameState {
    private GameController controller;

    public GameState(GameController controller) {
        this.controller = controller;
    }

    protected GameController getController() {
        return controller;
    }

    public abstract void onTouchCellDown(Player player, int x, int y, int pointerId);

    public abstract void onTouchCell(Player player, int x, int y, int pointerId);

    public abstract void onTouchCellUp(Player player, int x, int y, int pointerId);
}

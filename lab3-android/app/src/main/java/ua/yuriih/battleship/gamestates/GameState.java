package ua.yuriih.battleship.gamestates;

import ua.yuriih.battleship.GameController;
import ua.yuriih.battleship.model.Player;

public abstract class GameState {
    private GameController controller;

    public GameState(GameController controller) {
        this.controller = controller;
    }

    public void start() {}

    protected GameController getController() {
        return controller;
    }

    public void onTouchCellDown(Player player, int x, int y, int pointerId) {}

    public void onTouchCell(Player player, int x, int y, int pointerId) {}

    public void onTouchCellUp(Player player, int x, int y, int pointerId) {}
}

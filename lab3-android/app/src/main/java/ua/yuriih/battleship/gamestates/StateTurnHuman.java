package ua.yuriih.battleship.gamestates;

import ua.yuriih.battleship.GameController;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class StateTurnHuman extends GameState {
    private boolean hit;
    private boolean destroyed;

    public StateTurnHuman(GameController controller) {
        super(controller);
    }

    @Override
    public void onTouchCellUp(Player player, int x, int y, int pointerId) {
        GameController controller = getController();
        if (player == Player.HUMAN)
            return;

        GameField aiField = controller.getField(Player.AI);
        CellState cell = aiField.getCell(x, y);
        switch (cell) {
            case EMPTY:
                aiField.setCell(x, y, CellState.MISSED);
                break;
            case SHIP:
                aiField.setCell(x, y, CellState.HIT);
                hit = true;
                destroyed = getController().checkShipDestroyed(Player.AI, x, y, true);
                break;
            case HIT:
            case MISSED:
                getController().vibrateForFailure();
                return;
        }

        if (hit) {
            if (destroyed && controller.checkVictory(Player.HUMAN))
                controller.setNextState(new StateGameEnd(Player.HUMAN, controller));
            else
                controller.setNextState(new StateTurnHuman(controller));
        } else {
            controller.setNextState(new StateTurnsAI(controller));
        }

        controller.redrawUI();
        controller.startNextState();
    }

    public boolean hit() {
        return hit;
    }

    public boolean destroyed() {
        return destroyed;
    }
}

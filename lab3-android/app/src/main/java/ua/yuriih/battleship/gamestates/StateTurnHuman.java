package ua.yuriih.battleship.gamestates;

import ua.yuriih.battleship.GameController;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class StateTurnHuman extends GameState {
    private boolean missed;
    private boolean hit;
    private boolean destroyed;

    public StateTurnHuman(GameController controller) {
        super(controller);
    }

    @Override
    public void onTouchCellUp(Player player, int x, int y, int pointerId) {
        if (player == Player.AI) {
            GameField aiField = getController().getField(Player.AI);
            CellState cell = aiField.getCell(x, y);
            switch (cell) {
                case EMPTY:
                    aiField.setCell(x, y, CellState.MISSED);
                    missed = true;
                    break;
                case SHIP:
                    aiField.setCell(x, y, CellState.HIT);
                    hit = true;
                    destroyed = getController().checkShipDestroyed(Player.AI, x, y);
                    break;
                case HIT:
                case MISSED:
                    getController().vibrateForFailure();
                    return;
            }
        }

        if (missed) {
            getController().setNextState(new StateTurnAI(getController()));
        }

        if (hit) {
            if (destroyed && getController().checkVictory(Player.HUMAN))
                getController().setNextState(new StateGameEnd(Player.HUMAN, getController()));
            else
                getController().setNextState(new StateTurnHuman(getController()));
            getController().redrawUI();
        }
        getController().startNextState();
    }

    public boolean missed() {
        return missed;
    }

    public boolean hit() {
        return hit;
    }

    public boolean destroyed() {
        return destroyed;
    }
}

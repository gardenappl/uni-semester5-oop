package ua.yuriih.battleship.gamestates;

import ua.yuriih.battleship.GameController;
import ua.yuriih.battleship.model.Player;

public class StateGameEnd extends GameState {
    private final Player winner;

    public StateGameEnd(Player winner, GameController controller) {
        super(controller);
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }
}

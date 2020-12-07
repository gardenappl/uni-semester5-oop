package ua.yuriih.battleship;

import ua.yuriih.battleship.gamestates.StateTurnsAI;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateTurnsAITest {
    @Test
    public void shouldHitNearHalfDamaged() {
        GameController controller = new GameController();
        GameField field = controller.getField(Player.HUMAN);

        field.setCell(1, 1, CellState.HIT);
        field.setCell(1, 2, CellState.SHIP);

        controller.setNextState(new StateTurnsAI(controller));
        controller.startNextState();

        //AI should try hitting around (1, 1)

        boolean triedHit = false;
        searchLoop:
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (!(x == 1 && y == 1) && (field.getCell(x, y) == CellState.HIT ||
                        field.getCell(x, y) == CellState.MISSED)) {
                    triedHit = true;
                    break searchLoop;
                }
            }
        }
        assertTrue(triedHit);
    }
}

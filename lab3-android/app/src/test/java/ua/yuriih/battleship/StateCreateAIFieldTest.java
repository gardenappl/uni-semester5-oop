package ua.yuriih.battleship;

import org.junit.Test;

import ua.yuriih.battleship.gamestates.StateCreateAIField;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

import static org.junit.Assert.*;

public class StateCreateAIFieldTest {
    @Test
    public void createField() {
        GameController controller = new GameController();

        for (int i = 0; i < 1000; i++) {
            controller.setNextState(new StateCreateAIField(controller));
            for (int x = 0; x < controller.getWidth(); x++) {
                for (int y = 0; y < controller.getHeight(); y++) {
                    controller.getField(Player.AI).setCell(x, y, CellState.EMPTY);
                }
            }
            controller.startNextState();
            assertTrue(controller.isValidField(controller.getField(Player.AI)));
        }
    }
}

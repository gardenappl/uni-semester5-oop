package ua.yuriih.battleship;

import org.junit.Test;

import ua.yuriih.battleship.gamestates.StateCreateAIField;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

import static org.junit.Assert.*;

public class GameControllerTests {
    @Test
    public void isTouching() {
        GameController controller = new GameController();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                if (x == -2 && y == -1)
                    assertTrue(controller.isTouching(x, y, -1, -1));
                else if (x == -1 && y == -2)
                    assertTrue(controller.isTouching(x, y, -1, -1));
                else if (x == 0 && y == -1)
                    assertTrue(controller.isTouching(x, y, -1, -1));
                else if (x == -1 && y == 0)
                    assertTrue(controller.isTouching(x, y, -1, -1));
                else
                    assertFalse(controller.isTouching(x, y, -1, -1));
            }
        }
    }

    @Test
    public void getMaxShipCount() {
        GameController controller = new GameController();
        assertEquals(4, controller.getMaxShipCount(1));
        assertEquals(3, controller.getMaxShipCount(2));
        assertEquals(2, controller.getMaxShipCount(3));
        assertEquals(1, controller.getMaxShipCount(4));

        boolean thrownException = true;
        try {
            controller.getMaxShipCount(0);
            thrownException = false;
        } catch (IllegalArgumentException ignored) {}
        assertTrue(thrownException);
    }

    @Test
    public void checkShipDestroyed() {
        GameController controller = new GameController();
        GameField aiField = controller.getField(Player.AI);

        aiField.setCell(4, 4, CellState.HIT);
        aiField.setCell(4, 5, CellState.HIT);
        aiField.setCell(5, 4, CellState.HIT);

        aiField.setCell(9, 9, CellState.SHIP);
        aiField.setCell(9, 8, CellState.HIT);

        assertTrue(controller.checkShipDestroyed(Player.AI, 4, 4, false));
        assertTrue(controller.checkShipDestroyed(Player.AI, 4, 5, false));
        assertTrue(controller.checkShipDestroyed(Player.AI, 5, 4, true));

        assertFalse(controller.checkShipDestroyed(Player.AI, 3, 4, true));
        assertFalse(controller.checkShipDestroyed(Player.AI, 9, 8, true));
        assertFalse(controller.checkShipDestroyed(Player.AI, 9, 9, true));

        assertEquals(aiField, TestUtils.createFieldFromArray(10, 10, new int[][]{
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,3,3,3,3,0,0,0},
                new int[] {0,0,0,3,2,2,3,0,0,0},
                new int[] {0,0,0,3,2,3,3,0,0,0},
                new int[] {0,0,0,3,3,3,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,2},
                new int[] {0,0,0,0,0,0,0,0,0,1}
        }));
    }

    @Test
    public void isValidField() {
        GameController controller = new GameController();
        GameField testField_notEnoughShips = TestUtils.createFieldFromArray(10, 10, new int[][]{
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,3,3,3,3,0,0,0},
                new int[] {0,0,0,3,2,2,3,0,0,0},
                new int[] {0,0,0,3,2,3,3,0,0,0},
                new int[] {0,0,0,3,3,3,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,2},
                new int[] {0,0,0,0,0,0,0,0,0,1}
        });
        assertFalse(controller.isValidField(testField_notEnoughShips));

        GameField testField = TestUtils.createFieldFromArray(10, 10, new int[][]{
                new int[] {0,1,0,1,0,1,0,1,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,1,1,0,1},
                new int[] {0,0,0,3,3,3,3,0,0,1},
                new int[] {0,0,0,3,2,2,3,0,0,0},
                new int[] {0,0,0,3,2,3,3,0,1,0},
                new int[] {0,0,0,3,3,3,0,1,1,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,1,1,1,1,0,0,0,2},
                new int[] {0,0,0,0,0,0,0,0,0,1}
        });
        assertTrue(controller.isValidField(testField));

        GameField testField_adjacentShips = TestUtils.createFieldFromArray(10, 10, new int[][]{
                new int[] {0,1,0,1,0,1,0,0,0,0},
                new int[] {0,0,1,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,1,1,0,1},
                new int[] {0,0,0,3,3,3,3,0,0,1},
                new int[] {0,0,0,3,2,2,3,0,0,0},
                new int[] {0,0,0,3,2,3,3,0,1,0},
                new int[] {0,0,0,3,3,3,0,0,1,0},
                new int[] {0,0,0,0,0,0,0,0,1,0},
                new int[] {0,0,1,1,1,1,0,0,0,2},
                new int[] {0,0,0,0,0,0,0,0,0,1}
        });
        assertFalse(controller.isValidField(testField_adjacentShips));

        GameField testField_shipTooLong = TestUtils.createFieldFromArray(10, 10, new int[][]{
                new int[] {0,1,0,1,0,1,0,1,0,0},
                new int[] {0,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,0,0,0,0,1,1,0,1},
                new int[] {1,0,0,3,3,3,3,0,0,1},
                new int[] {1,0,0,3,2,2,3,0,0,0},
                new int[] {1,0,0,3,2,3,3,0,1,0},
                new int[] {1,0,0,3,3,3,0,1,1,0},
                new int[] {1,0,0,0,0,0,0,0,0,0},
                new int[] {0,0,1,1,1,1,0,0,0,2},
                new int[] {0,0,0,0,0,0,0,0,0,1}
        });
        assertFalse(controller.isValidField(testField_shipTooLong));
    }

    @Test
    public void checkVictory() {
        GameController controller = new GameController();
        controller.setNextState(new StateCreateAIField(controller));
        controller.startNextState();

        assertFalse(controller.checkVictory(Player.HUMAN));

        GameField aiField = controller.getField(Player.AI);
        for (int x = 0; x < controller.getWidth(); x++) {
            for (int y = 0; y < controller.getHeight(); y++) {
                if (aiField.getCell(x, y) == CellState.SHIP)
                    aiField.setCell(x, y, CellState.HIT);
            }
        }

        assertTrue(controller.checkVictory(Player.HUMAN));
    }
}

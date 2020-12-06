package ua.yuriih.battleship.gamestates;

import android.graphics.Point;
import android.util.Log;

import java.util.Random;

import ua.yuriih.battleship.GameController;
import ua.yuriih.battleship.model.CellState;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;

public class StateCreateAIField extends GameState {
    private static final String LOGGING_TAG = "CreateAIField";

    public StateCreateAIField(GameController controller) {
        super(controller);
    }

    @Override
    public void start() {
        GameController controller = getController();
        GameField aiField = controller.getField(Player.AI);
        /*for (int x = 0; x < controller.getWidth(); x++) {
            for (int y = 0; y < controller.getHeight(); y++)
                aiField.setCell(x, y, CellState.EMPTY);
        }*/
        for (int size = 4; size >= 1; size--) {
            for (int j = 0; j < controller.getMaxShipCount(size); j++)
                placeRandomPiece(size, aiField);
        }
//        controller.redrawUI();
        Log.d(LOGGING_TAG, "Created AI field");
    }

    private Point makeRandomPoint() {
        GameController controller = getController();

        return new Point(controller.getRNG().nextInt(controller.getWidth()),
                controller.getRNG().nextInt(controller.getHeight()));
    }

    private Point getRandomTouchingPoint(Point point) {
        Random rng = getController().getRNG();
        randomLoop:
        while (true) {
            int direction = rng.nextInt(4);
            switch (direction) {
                case 0:
                    if (point.x > 0)
                        return new Point(point.x - 1, point.y);
                    continue randomLoop;
                case 1:
                    if (point.x < getController().getWidth() - 1)
                        return new Point(point.x + 1, point.y);
                    continue randomLoop;
                case 2:
                    if (point.y > 0)
                        return new Point(point.x, point.y - 1);
                    continue randomLoop;
                case 3:
                    if (point.y < getController().getHeight() - 1)
                        return new Point(point.x, point.y + 1);
                    continue randomLoop;
            }
        }
    }

    public void placeRandomPiece(int size, GameField aiField) {
        Point[] figurePoints = new Point[size];

        randomLoop:
        while (true) {
            Point lastPoint = makeRandomPoint();
            if (!aiField.isValidCellForShip(lastPoint))
                continue;

            figurePoints[0] = lastPoint;

            for (int i = 1; i < size; i++) {
                Point point = getRandomTouchingPoint(lastPoint);
                for (int j = 0; j < i; j++) {
                    if (figurePoints[j].equals(point))
                        continue randomLoop;
                }
                if (!aiField.isValidCellForShip(point))
                    continue randomLoop;
                figurePoints[i] = point;
            }
            break;
        }
        for (int i = 0; i < size; i++)
            aiField.setCell(figurePoints[i].x, figurePoints[i].y, CellState.SHIP);
        Log.d(LOGGING_TAG, "Placed ship with size " + size);
    }
}

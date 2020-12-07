package ua.yuriih.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ua.yuriih.battleship.gamestates.GameState;
import ua.yuriih.battleship.gamestates.StateCreateHumanField;
import ua.yuriih.battleship.gamestates.StateGameEnd;
import ua.yuriih.battleship.gamestates.StateTurnHuman;
import ua.yuriih.battleship.gamestates.StateTurnsAI;
import ua.yuriih.battleship.model.GameField;
import ua.yuriih.battleship.model.Player;
import ua.yuriih.battleship.model.Point;

public class GameActivity extends AppCompatActivity {
    private static final String LOGGING_TAG = "GameActivity";
    private GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        final GameFieldView fieldView = findViewById(R.id.game_field);
        final Button switchButton = findViewById(R.id.switch_perspective);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldView.getShownPlayer() == Player.HUMAN) {
                    focusOnAI();
                } else {
                    focusOnHuman();
                }
            }
        });

        Button restartButton = findViewById(R.id.restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        startNewGame();
    }

    private void startNewGame() {
        GameFieldView fieldView = findViewById(R.id.game_field);
        controller = new GameController();
        controller.registerView(fieldView);
        controller.setGameActivity(this);
        fieldView.setGameController(controller);

        controller.setNextState(new StateCreateHumanField(controller));
        controller.startNextState();

        focusOnHuman();
        fieldView.revealOpponent(false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void focusOnAI() {
        GameFieldView gameField = findViewById(R.id.game_field);
        gameField.showPlayer(Player.AI);
        Button switchButton = findViewById(R.id.switch_perspective);
        switchButton.setText(R.string.switch_to_player);
    }

    private void focusOnHuman() {
        GameFieldView gameField = findViewById(R.id.game_field);
        gameField.showPlayer(Player.HUMAN);
        Button switchButton = findViewById(R.id.switch_perspective);
        switchButton.setText(R.string.switch_to_ai);
    }

    public void onStateChange(GameState oldState, GameState newState) {
        Log.d(LOGGING_TAG, "Old: " + oldState);
        Log.d(LOGGING_TAG, "New: " + newState);

        if (newState instanceof StateCreateHumanField)
            findViewById(R.id.switch_perspective).setVisibility(View.GONE);
        else
            findViewById(R.id.switch_perspective).setVisibility(View.VISIBLE);

        if (newState instanceof StateGameEnd)
            findViewById(R.id.restart).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.restart).setVisibility(View.GONE);


        TextView gameStateLabel = findViewById(R.id.game_state_label);

        if (newState instanceof StateCreateHumanField) {
            gameStateLabel.setText(R.string.draw_your_field);
        } else if (newState instanceof StateGameEnd) {
            GameFieldView fieldView = findViewById(R.id.game_field);
            fieldView.revealOpponent(true);

            if (((StateGameEnd) newState).getWinner() == Player.HUMAN) {
                gameStateLabel.setText(R.string.you_win);
                focusOnAI();
            } else {
                gameStateLabel.setText(R.string.you_lose);
                focusOnHuman();
            }
        } else if (oldState instanceof StateTurnsAI) {
            if (((StateTurnsAI) oldState).getDestroyedShipCount() == 1) {
                Point lastSinkPoint = ((StateTurnsAI) oldState).getLastSinkPoint();
                gameStateLabel.setText(getString(R.string.ai_sunk, lastSinkPoint));

            } else if (((StateTurnsAI) oldState).getDestroyedShipCount() > 1) {
                int destroyedCount = ((StateTurnsAI) oldState).getDestroyedShipCount();
                gameStateLabel.setText(getResources().getQuantityString(R.plurals.ai_sunk_multiple,
                        destroyedCount, destroyedCount));

            } else if (((StateTurnsAI) oldState).hit()) {
                Point lastHitPoint = ((StateTurnsAI) oldState).getLastHitPoint();
                gameStateLabel.setText(getString(R.string.ai_hit, lastHitPoint));

            } else {
                Point missPoint = ((StateTurnsAI) oldState).getMissPoint();
                gameStateLabel.setText(getString(R.string.ai_missed, missPoint));
            }
            focusOnHuman();
        } else if (oldState instanceof StateTurnHuman) {
            if (((StateTurnHuman) oldState).destroyed())
                gameStateLabel.setText(R.string.you_sunk);
            else if (((StateTurnHuman) oldState).hit())
                gameStateLabel.setText(R.string.you_hit);
            else
                gameStateLabel.setText(R.string.you_missed);
        } else if (newState instanceof StateTurnHuman) {
            gameStateLabel.setText(R.string.your_turn);
        }
    }
}
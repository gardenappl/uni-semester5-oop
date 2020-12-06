package ua.yuriih.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ua.yuriih.battleship.gamestates.GameState;
import ua.yuriih.battleship.gamestates.StateCreatePlayerField;
import ua.yuriih.battleship.model.Player;

public class GameActivity extends AppCompatActivity {
    private GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        final GameFieldView fieldView = findViewById(R.id.game_field);
        controller = new GameController(getApplicationContext());
        controller.registerView(fieldView);
        controller.setGameActivity(this);
        fieldView.setGameController(controller);
        fieldView.revealOpponent(true);

        controller.setNextState(new StateCreatePlayerField(controller));
        controller.startNextState();

        final Button switchButton = findViewById(R.id.switch_perspective);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldView.getShownPlayer() == Player.HUMAN) {
                    fieldView.showPlayer(Player.AI);
                    switchButton.setText(R.string.switch_to_player);
                } else {
                    fieldView.showPlayer(Player.HUMAN);
                    switchButton.setText(R.string.switch_to_ai);
                }
            }
        });
        switchButton.setVisibility(View.GONE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void onStateChange(GameState oldState, GameState newState) {
        if (!(newState instanceof StateCreatePlayerField))
            findViewById(R.id.switch_perspective).setVisibility(View.VISIBLE);
    }
}
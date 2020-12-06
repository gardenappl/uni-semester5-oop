package ua.yuriih.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import ua.yuriih.battleship.gamestates.StateCreatePlayerField;

public class GameActivity extends AppCompatActivity {
    private GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        GameFieldView fieldView = findViewById(R.id.game_field);
        controller = new GameController(getApplicationContext());
        controller.registerView(fieldView);
        fieldView.setGameController(controller);

        controller.setNextState(new StateCreatePlayerField(controller));
        controller.startNextState();

        findViewById(R.id.switch_perspective).setVisibility(View.GONE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
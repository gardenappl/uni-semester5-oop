package ua.yuriih.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class GameActivity extends AppCompatActivity {
    private GameController state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        GameFieldView fieldView = findViewById(R.id.game_field);
        state = new GameController(getApplicationContext());
        state.registerView(fieldView);
        fieldView.setGameController(state);

        findViewById(R.id.switch_perspective).setVisibility(View.GONE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
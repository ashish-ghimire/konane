package com.example.raa_tey.kon2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        String namePlayer1 = intent.getStringExtra(GameActivity.PLAYER1_NAME_TEXT_BOX);
        String namePlayer2 = intent.getStringExtra(GameActivity.PLAYER2_NAME_TEXT_BOX);

        String scorePlayer1 = intent.getStringExtra(GameActivity.PLAYER1_SCORE_TEXT_BOX);
        String scorePlayer2 = intent.getStringExtra(GameActivity.PLAYER2_SCORE_TEXT_BOX);
        String winnerName = intent.getStringExtra(GameActivity.WINNER);

        displayFinalMessages(namePlayer1, namePlayer2, scorePlayer1, scorePlayer2, winnerName);
    }

    private void displayFinalMessages(String namePlayer1, String namePlayer2, String scorePlayer1, String scorePlayer2, String winnerName)
    {
        TextView textView = findViewById(R.id.gameOverPlayer1Name);
        textView.setText(namePlayer1);

        textView = findViewById(R.id.gameOverPlayer2Name);
        textView.setText(namePlayer2);

        textView = findViewById(R.id.gameOverPlayer1Score);
        textView.setText(scorePlayer1);

        textView = findViewById(R.id.gameOverPlayer2Score);
        textView.setText(scorePlayer2);

        textView = findViewById(R.id.gameOverWinner);
        textView.setText(winnerName);
    }
}

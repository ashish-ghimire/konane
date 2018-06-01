package com.example.raa_tey.kon2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String NAME_PLAYER1_KEY = "com.example.raa_tey.kon2.MESSAGE1";
    public static final String NAME_PLAYER2_KEY = "com.example.raa_tey.kon2.MESSAGE2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /** Called when the user taps the Send button */
    public void startTheGame(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, GameActivity.class);

        EditText editText = (EditText) findViewById(R.id.player1NameID);
        String message = editText.getText().toString();
        intent.putExtra(NAME_PLAYER1_KEY, message);

        EditText editText2 = (EditText) findViewById(R.id.player2NameID);
        String message2 = editText2.getText().toString();
        intent.putExtra(NAME_PLAYER2_KEY, message2);

        startActivity(intent);
    }
}

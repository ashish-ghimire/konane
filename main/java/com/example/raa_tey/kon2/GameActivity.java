package com.example.raa_tey.kon2;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GameActivity extends AppCompatActivity {

    private char BLACK = Board.getBlackStone();
    private char WHITE = Board.getWhiteStone();
    private int clickCount = 0;
    private int startRow = -1;
    private int startCol = -1;
    private int endRow = -1;
    private int endCol = -1;
    private int previousEndRow = -1; //previousEndRow and previousEndCol are needed if one move has more than one possible hop
    private int previousEndCol = -1;
//    private int previousStartRow = -1;
//    private int

    private final boolean BLACK_TURN = false;

    private boolean turn = BLACK_TURN;
    private boolean oneMoveHasMoreThanOneHop = false;

    public static final String PLAYER1_NAME_TEXT_BOX = "com.example.raa_tey.kon2.player1Name";
    public static final String PLAYER2_NAME_TEXT_BOX = "com.example.raa_tey.kon2.player2Name";
    public static final String PLAYER1_SCORE_TEXT_BOX = "com.example.raa_tey.kon2.player1Score";
    public static final String PLAYER2_SCORE_TEXT_BOX = "com.example.raa_tey.kon2.player2Score";
    public static final String WINNER = "com.example.raa_tey.kon2.winner";

    Game newGame = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
        setUpBoard();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String namePlayer1 = intent.getStringExtra(MainActivity.NAME_PLAYER1_KEY);
        String namePlayer2 = intent.getStringExtra(MainActivity.NAME_PLAYER2_KEY);

        initializeNameAndScore(namePlayer1, namePlayer2);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setClickable(false);
        Fragment f = new Fragment();
        f.setRetainInstance(true);
    }


    private void initializeNameAndScore(String namePlayer1, String namePlayer2) {

        newGame.getPlayingBlack().setColor(BLACK);
        newGame.getPlayingWhite().setColor(WHITE);

        newGame.getPlayingBlack().setName( namePlayer1 );
        newGame.getPlayingWhite().setName( namePlayer2 );

        TextView textView = findViewById(R.id.player1Name);
        textView.setText(namePlayer1);

        textView = findViewById(R.id.player2Name);
        textView.setText(namePlayer2);

        textView = findViewById(R.id.player1Score);
        textView.setText(newGame.getPlayingBlack().getScore() + "");

        textView = findViewById(R.id.player2Score);
        textView.setText(newGame.getPlayingWhite().getScore() + "");

        updateBoard(newGame.getGameBoard());
    }

    private void setUpBoard()
    {
        // attach the table layout element in the .xml file to board variable
        TableLayout board = findViewById(R.id.tableLayOutid);
        int count = 0;

        for(int i = 0; i < Board.MAX_STONES; i++)
        {
            TableRow row = new TableRow(this);

            for(int j = 0; j < Board.MAX_STONES; j++)
            {
                Button col1 = new Button(this);
                col1.setId(count);

                col1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Code here executes on main thread after user presses button
                        respond(v);
                    }
                });

                //The two lines below adjust the size of an individual button
                int size = getButtonSize();
                col1.setLayoutParams(new TableRow.LayoutParams(size, size));

                if( (i+j) % 2 == 0){
                    col1.setBackgroundColor(Color.BLACK); //setBlackBackground
                    col1.setTextColor(Color.WHITE);  //set White text in black background
                }
                else {
                    col1.setBackgroundColor(Color.WHITE);    //setWhiteBackground
                    col1.setTextColor(Color.BLACK);         //set Black text in black background
                }

                row.addView(col1);
                count++;
            }
            board.addView(row);
        }
    }

    private Player oppositePlayer(Player player){
        if( player.getColor() == newGame.getPlayingBlack().getColor() &&  player.getName().equals(newGame.getPlayingBlack().getName())
                && player.getScore() == newGame.getPlayingBlack().getScore() )
            return newGame.getPlayingWhite();
        else
            return newGame.getPlayingBlack();
    }

    //The function below returns a button size that adjusts appropriately with adjustments to screen resolution
    private int getButtonSize(){
        int density= getResources().getDisplayMetrics().densityDpi;
        int size =100;
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                size = 100;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                size = 100;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                size = 150;
                break;
        }

        return size;
    }

    //The function is responsible for integrating Model classes' logic with the View class, GameActivity.java
    public void hop( View view ) {
        Player player;

        if(turn == BLACK_TURN)
            player = newGame.getPlayingBlack();
        else
            player = newGame.getPlayingWhite();

        if(oneMoveHasMoreThanOneHop && !(previousEndRow == startRow && previousEndCol == startCol) )
            return;

        if ((newGame.verifyMoveLegality(startRow, startCol, endRow, endCol, newGame.getGameBoard(), player))) {
            newGame.makeAHop(newGame.getGameBoard(), startRow, startCol, endRow, endCol, player.getColor());
            updateScore(player);
            clickCount = 0;

            if (newGame.doOtherHopsExist(endRow, endCol, newGame.getGameBoard(), player)) {
                oneMoveHasMoreThanOneHop = true;
                previousEndCol = endCol;
                previousEndRow = endRow;

            } else {
                changeTurn();
                oneMoveHasMoreThanOneHop = false;
            }
        }
        else {
            resetRowCol();
//            clickCount = 0;
            return;
        }

        if(!oneMoveHasMoreThanOneHop) {
            if (! newGame.validMoveExists(oppositePlayer(player), newGame.getGameBoard()))
                changeTurn();
        }

        updateBoard(newGame.getGameBoard());

        if( newGame.gameOver() ){
            startFinalActivity();
        }
    }


    private void changeTurn(){
        resetRowCol();
        turn = !turn;
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.toggle();
    }

    private void startFinalActivity() {
        Intent intent = new Intent(this, GameOverActivity.class);

        TextView text = (TextView) findViewById(R.id.player1Name);
        String message = text.getText().toString();
        intent.putExtra(PLAYER1_NAME_TEXT_BOX, message);

        text = (TextView) findViewById(R.id.player1Score);
        message = text.getText().toString();
        intent.putExtra(PLAYER1_SCORE_TEXT_BOX, message);

        text = (TextView) findViewById(R.id.player2Name);
        message = text.getText().toString();
        intent.putExtra(PLAYER2_NAME_TEXT_BOX, message);

        text = (TextView) findViewById(R.id.player2Score);
        message = text.getText().toString();
        intent.putExtra(PLAYER2_SCORE_TEXT_BOX, message);

        message = newGame.nameOfWinner(newGame.getPlayingBlack(), newGame.getPlayingWhite());
        intent.putExtra(WINNER, message);

        startActivity(intent);
    }


    private void updateScore(Player player){
        player.setScore(1);
        int playerScore = player.getScore();

        if(player.getColor() == BLACK){
            TextView textView = findViewById(R.id.player1Score);
            textView.setText("" + playerScore);
        }
        else {
            TextView textView = findViewById(R.id.player2Score);
            textView.setText("" + playerScore);
        }
    }

    public void respond( View view ) {
        int count = 0; //The button id's are from 0 to 35. Button id's are indicated by count
        System.out.println("" + view.getId());

        for (int row = 0; row < Board.MAX_STONES; row++) {
            for (int col = 0; col < Board.MAX_STONES; col++) {

                if (view.getId() == count)
                    recordRowColumn(row, col);

                count++;
            }
        }
    }

    private void recordRowColumn(int row, int col){
        clickCount++;   //We are here means button has been pressed at least once

        if(clickCount % 2 == 1) {  //Reaching here means clickCount is at least 1
            startRow = row;
            startCol = col;
        }
        else {

//            if(row == startRow && col == startCol)
//                return;

            endRow = row;
            endCol = col;

            //clickCount =0; //Two clicks means possibly a move. Possibly a move means reset clickCount

            //so that it will be easier to track the next move
        }
    }

   private void updateBoard( Board board){
        int count = 0;
        for (int row = 0; row < board.MAX_STONES; row++) {
            for (int col = 0; col < board.MAX_STONES; col++) {

                int resourceId = count;
                ((Button)findViewById(resourceId)).setText(""+ board.getOneBoardElement(row, col));
                count++;
            }
        }
    }

    private void resetRowCol(){
        startRow = -1;
        startCol = -1;
        endRow = -1;
        endCol = -1;
        //previousEndRow = -1;
        //previousEndCol = -1;
        clickCount = 0;
    }
}

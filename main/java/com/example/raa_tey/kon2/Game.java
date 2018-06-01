package com.example.raa_tey.kon2;

import java.util.Scanner;

class Game
{
    private Player playingBlack;
    private Player playingWhite;
    private Board gameBoard;

    private int MAX_ROW = gameBoard.getMaxStones();
    private char BLACK = gameBoard.getBlackStone();
    private char WHITE = gameBoard.getWhiteStone();
    private char EMPTY = gameBoard.getEmptyStone();

    public Game() {
        playingBlack = new Player();
        playingWhite = new Player();
        gameBoard = new Board();
    }

    public Player getPlayingBlack() {
        return playingBlack;
    }

    public Player getPlayingWhite() {
        return playingWhite;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    private static String getPlayersName(int playerNumber)
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter name for player, " + playerNumber);
        String name;
        name = reader.nextLine();

        return name;
    }

    //public void main is just a test function that I used to test game logic on console
    public void main( String args[] ) {
        Game newGame = new Game();

        newGame.playingBlack.setColor(BLACK);
        newGame.playingWhite.setColor(WHITE);

        (newGame.playingBlack).setName( newGame.getPlayersName(1) );
        (newGame.playingWhite).setName( newGame.getPlayersName(2) );

        int passCount;

        do {
            passCount = 0;

            //Player playing black moves first
            System.out.println("It is " + newGame.playingBlack.getName() + " 's turn!");
            System.out.println(newGame.playingBlack.getName() + " 's color is " + newGame.playingBlack.getColor() );

            //Check if player playing black has valid moves
            if(validMoveExists(newGame.playingBlack, newGame.gameBoard))
                makeACompleteMove(newGame.gameBoard, newGame.playingBlack);
            else
                passCount++;

            //WHITEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            System.out.println("It is " + newGame.playingWhite.getName() + " 's turn!");
            System.out.println(newGame.playingWhite.getName() + " 's color is " + newGame.playingWhite.getColor() );

            if(validMoveExists(newGame.playingWhite, newGame.gameBoard))
                makeACompleteMove(newGame.gameBoard, newGame.playingWhite);
            else
                passCount++;

        } while( passCount < 2);

        //Declare the winner
        System.out.println("GAME OVERRRRRRRRR!!!");
        System.out.println("The winner is " + nameOfWinner(newGame.playingBlack, newGame.playingWhite));
    }

    //This function is used by the View class, GameActivity.java
    public void makeAHop(Board gameBoard, int startRow, int startCol, int endRow, int endCol, char playerColor) {
        gameBoard.setGameBoard(startRow, startCol, EMPTY);
        gameBoard.setGameBoard(endRow, endCol, playerColor);
        int middle;

        if(endRow == startRow) { //We moved either left or right, only col changes
            middle = (startCol + endCol) / 2;
            gameBoard.setGameBoard(startRow, middle, EMPTY);
        }
        else{
            middle = (startRow + endRow) / 2;
            gameBoard.setGameBoard(middle, startCol, EMPTY);
        }
    }

    //This function is used by the View class, GameActivity.java
    public boolean doOtherHopsExist(int endRow, int endCol, Board board, Player player) {
        char nextToNext = EMPTY;
        char next = ' ';

        switch (player.getColor()) {
            case 'B':
                next = WHITE;
                break;
            case 'W':
                next = BLACK;
                break;
        }

        if(checkRight(endRow, endCol, next, nextToNext, board) || checkLeft(endRow, endCol, next, nextToNext, board)
                || checkUp(endRow, endCol, next, nextToNext, board) || checkDown(endRow, endCol, next, nextToNext, board))
            return true;
        return false;
    }

    //This function is only used by Game.java to test logic when playing on console
    private void makeOtherHops(int endRow, int endCol, Board board, Player player) {
        char nextToNext = EMPTY;
        char next = ' ';

        switch (player.getColor()){
            case 'B':
                next = WHITE;
                break;
            case 'W':
                next = BLACK;
                break;
        }

        while (checkRight(endRow, endCol, next, nextToNext, board) || checkLeft(endRow, endCol, next, nextToNext, board)
                || checkUp(endRow, endCol, next, nextToNext, board) || checkDown(endRow, endCol, next, nextToNext, board))
        {
            System.out.println("Looks like you can make some other moves !");
            board.printBoard();



            int startRow = endRow;
            int startCol = endCol;

            do {

                endRow = getPoint("ending", "row");
                endCol = getPoint("ending", "col");

            } while (!(verifyPoint(endRow, endCol, player.getColor())
                    && verifyPositionDistance(startRow, startCol, endRow, endCol, board, player.getColor())));


            makeAHop(board, startRow, startCol, endRow, endCol, player.getColor());

            player.setScore(1);

            System.out.println(player.getName() + " 's score is " + player.getScore());
        }
    }

    public boolean verifyMoveLegality(int startRow, int startCol, int endRow, int endCol, Board board, Player player)
    {
        if(startRow < 0 || endRow < 0 || startCol < 0 || endCol < 0 )
            return false;

        if( (verifyPoint(startRow, startCol, player.getColor()) && verifyPoint(endRow, endCol, player.getColor())
                && verifyPositionDistance(startRow, startCol, endRow, endCol, board, player.getColor()) ) )
            return true;

        return false;
    }

    //This function is only used by Game.java to test logic when playing on console
    private void makeACompleteMove(Board board, Player player)
    {
        board.printBoard();
        int startRow, startCol, endRow, endCol;

        do {
            startRow = getPoint("starting", "row");
            startCol = getPoint("starting", "col");

            endRow = getPoint("ending", "row");
            endCol = getPoint("ending", "col");

        } while( ! (verifyPoint(startRow, startCol, player.getColor()) && verifyPoint(endRow, endCol, player.getColor())
                && verifyPositionDistance(startRow, startCol, endRow, endCol, board, player.getColor()) ) );


        //This is the actual making move part where stuff change
        makeAHop(board, startRow, startCol, endRow, endCol, player.getColor());

        //Update the score of playingBlack
        player.setScore(1);
        System.out.println(player.getName() + " 's score is " + player.getScore());

        //Check if other moves exist. Other moves should be based on the previous move
        makeOtherHops(endRow, endCol, board, player);
    }

    public static String nameOfWinner(Player player1, Player player2){
        if(player1.getScore() > player2.getScore())
            return  player1.getName();
        else if(player2.getScore() > player1.getScore())
            return player2.getName();
        return "It's a tie !!! ";
    }

    public boolean validMoveExists(Player player, Board board) {
        char nextToNext = EMPTY;
        char next;

        if(player.getColor() == BLACK)
            next = WHITE;
        else
            next = BLACK;

        for(int row = 0; row < MAX_ROW; row++)
        {
            for(int col = 0; col < MAX_ROW; col++){

                if(board.getGameBoard()[row][col] == player.getColor())
                {
                    if( checkLeft(row, col, next, nextToNext, board) || checkDown(row, col,next, nextToNext, board) ||
                            checkRight(row, col, next, nextToNext, board ) || checkUp(row, col, next, nextToNext, board) )
                        return true;
                }
            }
        }

        return false;
    }

    private static boolean checkUp(int row, int col, char next, char nextToNext, Board board) {
        if(row < 2)
            return false;

        if(board.getGameBoard()[row - 1][col] == next && board.getGameBoard()[row - 2][col] == nextToNext)
            return true;

        return false;
    }

    private boolean checkDown(int row, int col, char next, char nextToNext, Board board) {
        if(row > MAX_ROW - 3)
            return false;

        if(board.getGameBoard()[row+1][col] == next && board.getGameBoard()[row+2][col] == nextToNext)
            return true;

        return false;
    }

    private boolean checkRight(int row, int col, char next, char nextToNext, Board board) {
        if(col > MAX_ROW - 3)
            return false;

        if(board.getGameBoard()[row][col+1] == next && board.getGameBoard()[row][col+2] == nextToNext)
            return true;

        return false;
    }

    private boolean checkLeft(int row, int col, char next, char nextToNext, Board board) {
        if(col < 2)
            return false;

        if(board.getGameBoard()[row][col -1] == next && board.getGameBoard()[row][col - 2] == nextToNext)
            return true;
        return false;
    }

    private int getPoint(String startOrEnd, String rowOrCol){
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter "+ startOrEnd + " " + rowOrCol);
        int point;
        point = reader.nextInt();

        return point;
    }

    private boolean verifyPoint(int row, int col, char playerColor)
    {
        boolean checker = false;
        switch(playerColor){
            case 'B':
                boolean evenAndEven = isEven(row) && isEven(col);
                boolean oddAndOdd = isOdd(row) && isOdd(col);
                checker = evenAndEven || oddAndOdd;
                break;

            case 'W':
                checker = (isOdd(row) && isEven(col)) || (isEven(row) && isOdd(col));
                break;
        }

        return checker;
    }

    private boolean isEven(int number){
        if(number % 2 == 0)
            return true;
        return false;
    }

    private boolean isOdd(int number){
        if(number % 2 != 0)
            return true;
        return false;
    }

    private boolean verifyPositionDistance(int startRow, int startCol, int endRow, int endCol, Board board, char playerColor){
        char nextToNext = EMPTY;
        char next;

        if(playerColor == BLACK)
            next = WHITE;
        else
            next = BLACK;


        if( magnitude(endRow - startRow) != 2 && magnitude(endCol - startCol) != 2 )
            return false;

        if( !(startRow == endRow || startCol == endCol) )
            return false;

        if ( movedLeft(startRow, startCol, endRow, endCol, next, nextToNext, board)  || movedRight(startRow, startCol, endRow, endCol, next, nextToNext, board)
                || movedUp(startRow, startCol, endRow, endCol, next, nextToNext, board) || movedDown(startRow, startCol, endRow, endCol, next, nextToNext, board) )
            return true;

        return false;
    }

    private boolean movedLeft(int startRow, int startCol, int endRow, int endCol, char next, char nextToNext, Board board) {
        if( !checkLeft(startRow, startCol, next, nextToNext, board) )
            return false;

        if( (endRow == startRow) && (endCol == startCol - 2) )
            return true;

        return false;
    }

    private boolean movedRight(int startRow, int startCol, int endRow, int endCol, char next, char nextToNext, Board board) {
        if( !checkRight(startRow, startCol, next, nextToNext, board) )
            return false;

        if( (endRow == startRow) && (endCol == startCol + 2) )
            return true;

        return false;
    }

    private boolean movedUp(int startRow, int startCol, int endRow, int endCol, char next, char nextToNext, Board board) {
        if( !checkUp(startRow, startCol, next, nextToNext, board) )
            return false;

        if( (endCol == startCol) && (endRow == startRow - 2) )
            return true;

        return false;
    }

    private boolean movedDown(int startRow, int startCol, int endRow, int endCol, char next, char nextToNext, Board board) {
        if( !checkDown(startRow, startCol, next, nextToNext, board) )
            return false;

        if( (endCol == startCol) && (endRow == startRow + 2) )
            return true;

        return false;
    }


    private int magnitude(int number){
        if(number < 0)
            return -1 * number;

        return number;
    }

    public boolean gameOver(){

        if( ! (validMoveExists(playingBlack, gameBoard)
                || validMoveExists(playingWhite, gameBoard) ))
            return true;

        return false;
    }

}

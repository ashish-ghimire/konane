package com.example.raa_tey.kon2;

class Board
{
    public static final int MAX_STONES = 6;
    char[][] gameBoard = new char [MAX_STONES][MAX_STONES];
    private static final char BLACK_STONE = 'B';
    private static final char WHITE_STONE = 'W';
    private static final char EMPTY_STONE = ' ';

    public Board() {
        initializeBoard();
        removeOneRandomBlack();
        removeOneRandomWhite();
    }

    private void initializeBoard()
    {
        for(int row = 0; row < MAX_STONES; row++)
        {
            for(int col = 0; col < MAX_STONES; col++)
            {
                if( (row + col) % 2 == 0 )
                    gameBoard[row][col] = BLACK_STONE;
                else
                    gameBoard[row][col] = WHITE_STONE;
            }
        }
    }

    public void setGameBoard(int row, int col, char change) {
        this.gameBoard[row][col] = change;
    }

    private void removeOneRandomBlack()
    {
        int random1;
        int random2;
        boolean evenAndEven;
        boolean oddAndOdd;

        //Black stones are in spots where (row + col) == an even number
        do
        {
            random1 = (int) (Math.random() * MAX_STONES);
            random2 = (int) (Math.random() * (MAX_STONES) );
            evenAndEven = isEven(random1) && isEven(random2);
            oddAndOdd = isOdd(random1) && isOdd(random2);
        }while(! (evenAndEven || oddAndOdd)  );

        gameBoard[random1][random2] = EMPTY_STONE;
    }

    private void removeOneRandomWhite()
    {
        int random1;
        int random2;
        boolean evenAndOdd;
        boolean oddAndEven;

        //Black stones are in spots where (row + col) == an even number
        do
        {
            random1 = (int) (Math.random() * MAX_STONES);
            random2 = (int) (Math.random() * (MAX_STONES));
            evenAndOdd = isEven(random1) && isOdd(random2);
            oddAndEven = isOdd(random1) && isEven(random2);
        }while(! (evenAndOdd || oddAndEven ) );

        gameBoard[random1][random2] = EMPTY_STONE;
    }

    private boolean isEven(int aNumber)
    {
        if(aNumber % 2 == 0)
            return true;
        return false;
    }

    private boolean isOdd(int aNumber)
    {
        if(aNumber % 2 != 0)
            return true;
        return false;
    }

    public static int getMaxStones() {
        return MAX_STONES;
    }

    public static char getBlackStone() {
        return BLACK_STONE;
    }

    public static char getWhiteStone() {
        return WHITE_STONE;
    }

    public static char getEmptyStone() {
        return EMPTY_STONE;
    }

    public char[][] getGameBoard() {
        return gameBoard;
    }

    public char getOneBoardElement(int row, int col) {
        if(row >= MAX_STONES || col >= MAX_STONES)
            return 'F';
        return gameBoard[row][col];
    }

    public void printBoard() {
        for(int row = 0; row < MAX_STONES; row++)
        {
            for(int col = 0; col < MAX_STONES; col++)
            {
                System.out.print(gameBoard[row][col] + " ");
            }
            System.out.print("\n");
        }
    }
}

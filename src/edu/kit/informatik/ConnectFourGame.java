package edu.kit.informatik;

import edu.kit.informatik.exceptions.IllegalCommandException;

/**
 * Connect Four game. It contains the playing board, performs changes on it,
 * gets the board state and checks the playing state
 * 
 * @author Moayad Yaghi
 * @version 1.0
 *
 */
public class ConnectFourGame {
    /**
     * The game board must be 4x4 dimensioned square.
     */
    public static final int BOARD_DIMENSION = 8;
    /**
     * A player can have maximum 32 tokens.
     */
    public static final int MAXIMUM_TOKENS_NUMBER = 32;
    /**
     * A player can have minimum 28 tokens.
     */
    public static final int MINIMUM_TOKENS_NUMBER = 28;
    /**
     * The dead corner is the corner in the game board that can never contain a
     * winning sequence.
     */
    private static final int DEAD_CORNER = 2;

    private Cell[][] board = new Cell[BOARD_DIMENSION][BOARD_DIMENSION];
    private GameState gameState;
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private Player winner;

    /**
     * Creates a Connect Four game with two players and empty cells. Sets the player
     * one as the first player.
     * 
     * @param playerOne The first player.
     * @param playerTwo The second player.
     */
    public ConnectFourGame(Player playerOne, Player playerTwo) {
        gameState = GameState.RUNNING;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        currentPlayer = this.playerOne;
        winner = null;
        board = new Cell[BOARD_DIMENSION][BOARD_DIMENSION];
        setCellsToEmpty(board);
    }

    /**
     * Scans the entire board looking for a winner. A player is the winner if they
     * have at least 4 tokens followed by each other as a sequence in the board, it
     * could be horizontal, vertical or diagonal in both directions. The game ends
     * with the result 'draw' if:
     * <ul>
     * <li>the game's board is full of tokens
     * <li>when the rival has no more tokens
     * <li>when both players won at the same time (after flip or remove command).
     * </ul>
     */
    public void checkGameState() {
        searchHorizontally();
        searchVertically();
        leftSearchDiagonaly();
        rightSearchDiagonaly();
        if (playerOne.getWinningLine() > 0 || playerTwo.getWinningLine() > 0) {
            if (playerOne.getWinningLine() == playerTwo.getWinningLine())
                gameState = GameState.DRAW;
            else {
                gameState = GameState.WON;
                if (playerOne.getWinningLine() > playerTwo.getWinningLine())
                    winner = playerOne;
                else
                    winner = playerTwo;
            }
        } else if ((winner == null && boardIsFull()) || !currentPlayer.hasTokens())
            gameState = GameState.DRAW;
    }

    /**
     * Scans the board horizontally looking for four tokens with the same label.
     */
    private void searchHorizontally() {
        for (int row = BOARD_DIMENSION - 1; row >= 0; row--) {
            String rowSequence = "";
            for (int column = 0; column < BOARD_DIMENSION; column++) {
                rowSequence += board[column][row].toString();
            }
            checkWinningSequences(rowSequence);
        }
    }

    /**
     * Scans the board vertically looking for four tokens with the same label.
     */
    private void searchVertically() {
        for (int column = 0; column < BOARD_DIMENSION; column++) {
            String columnSequence = "";
            for (int row = BOARD_DIMENSION - 1; row >= 0; row--) {
                columnSequence += board[column][row].toString();
            }
            checkWinningSequences(columnSequence);
        }
    }

    /**
     * Scans the board diagonally looking for four tokens with the same label. This
     * diagonal is from the down-left of the board to the up-right (like this '/').
     */
    private void leftSearchDiagonaly() {
        String diagonalSequence;
        int column;
        int row = BOARD_DIMENSION - 1;
        int verticalLevel = 1;
        int rowLevel = 1;
        for (int horizontalLevel = BOARD_DIMENSION - 1; horizontalLevel > DEAD_CORNER; horizontalLevel--) {
            diagonalSequence = "";

            column = 0;
            for (row = horizontalLevel; row >= 0; row--) {
                diagonalSequence += board[column][row].toString();
                column++;
            }
            checkWinningSequences(diagonalSequence);

            diagonalSequence = "";
            column = verticalLevel;
            for (row = BOARD_DIMENSION - 1; row >= rowLevel; row--) {
                if (verticalLevel < BOARD_DIMENSION - 1 - DEAD_CORNER) {
                    diagonalSequence += board[column][row].toString();
                    column++;
                }
            }
            checkWinningSequences(diagonalSequence);
            rowLevel++;
            verticalLevel++;
        }
    }

    /**
     * Scans the board diagonally looking for four tokens with the same label. This
     * diagonal is from the down-right of the board to the up-left (like this '\').
     */
    private void rightSearchDiagonaly() {
        String diagonalSequence;
        int column;
        int row = BOARD_DIMENSION - 1;
        int verticalLevel = 6;
        int rowLevel = 1;
        for (int horizontalLevel = BOARD_DIMENSION - 1; horizontalLevel > DEAD_CORNER; horizontalLevel--) {

            diagonalSequence = "";
            column = 7;
            for (row = horizontalLevel; row >= 0; row--) {
                diagonalSequence += board[column][row].toString();
                column--;
            }
            checkWinningSequences(diagonalSequence);

            diagonalSequence = "";
            column = verticalLevel;
            for (row = BOARD_DIMENSION - 1; row >= rowLevel; row--) {
                if (verticalLevel > DEAD_CORNER) {
                    diagonalSequence += board[column][row].toString();
                    column--;
                }
            }
            checkWinningSequences(diagonalSequence);
            rowLevel++;
            verticalLevel--;
        }
    }

    /**
     * Checks the lines of the board if they contain winning sequences. If they do,
     * it counts the amount of them for each player.
     * 
     * @param winningLines The passed lines to be checked.
     */
    private void checkWinningSequences(String winningLines) {
        if (winningLines.contains(playerOne.getLabel().toString() + playerOne.getLabel().toString()
                + playerOne.getLabel().toString() + playerOne.getLabel().toString()))
            playerOne.increaseByOne();
        if (winningLines.contains(playerTwo.getLabel().toString() + playerTwo.getLabel().toString()
                + playerTwo.getLabel().toString() + playerTwo.getLabel().toString()))
            playerTwo.increaseByOne();
    }

    /**
     * Throws a token of the current player in a column that its number was input
     * after checking if it is not full of tokens. A throw reduces the number of
     * tokens of the current player. In case the throw was done successfully it
     * switches the player.
     * 
     * @param columnNumber The passed column number to be thrown in.
     * @throws IllegalCommandException If the column which its number was input was full.
     * @return {@code true} If throwin a token was done successfully, {@code false} otherwise.
     */
    public boolean throwin(int columnNumber) throws IllegalCommandException {
        if (board[columnNumber][0].equals(Cell.EMPTY_CELL)) {
            for (int row = BOARD_DIMENSION - 1; row >= 0; row--) {
                if (board[columnNumber][row].equals(Cell.EMPTY_CELL)) {
                    if (currentPlayer.getLabel().equals(Cell.P1))
                        board[columnNumber][row] = Cell.P1;
                    if (currentPlayer.getLabel().equals(Cell.P2))
                        board[columnNumber][row] = Cell.P2;
                    currentPlayer.reduceByOne();
                    nextPlayer();
                    return true;
                }
            }
        } else {
            throw new IllegalCommandException("the column in which you want to throw a token is full.");
        }
        return false;
    }

    /**
     * Flips the board upside down making the upper tokens down and vice versa.
     */
    public void flip() {
        Cell[][] afterFlipping = new Cell[BOARD_DIMENSION][BOARD_DIMENSION];
        setCellsToEmpty(afterFlipping);
        int newPosition;
        for (int column = 0; column < BOARD_DIMENSION; column++) {
            newPosition = BOARD_DIMENSION - 1;
            for (int row = 0; row < BOARD_DIMENSION; row++) {
                if (!board[column][row].equals(Cell.EMPTY_CELL)) {
                    afterFlipping[column][newPosition] = board[column][row];
                    newPosition--;

                }
            }
        }
        board = afterFlipping;
    }

    /**
     * Removes a token from the bottom of a column and sets all the tokens above one
     * step downward. A remove command can only be done if:
     * <ul>
     * <li>the desired token to be removed belongs to the currently active player.
     * <li>the chosen column is not empty
     * </ul>
     * 
     * @param columnNumber The input column number.
     * @return {@code true} If removing a token was done successfully, {@code false}
     *         otherwise.
     * @throws IllegalCommandException
     *             If the chosen column is empty or if the lower token of the chosen
     *             column belongs to the rival.
     */
    public boolean remove(int columnNumber) throws IllegalCommandException {
        if (board[columnNumber][BOARD_DIMENSION - 1].equals(currentPlayer.getLabel())) {
            for (int row = BOARD_DIMENSION - 1; row > 0; row--) {
                board[columnNumber][row] = board[columnNumber][row - 1];
            }
            board[columnNumber][0] = Cell.EMPTY_CELL;
            nextPlayer();
            return true;
        } else if (board[columnNumber][BOARD_DIMENSION - 1].equals(Cell.EMPTY_CELL))
            throw new IllegalCommandException("the column with the number you entered is empty.");
        else if (!board[columnNumber][BOARD_DIMENSION - 1].equals(currentPlayer.getLabel()))
            throw new IllegalCommandException("you cannot remove your rival's token.");
        return false;
    }

    /**
     * Checks if the board is full regardless of containing a winner.
     * 
     * @return {@code true} If the board is full, {@code false} otherwise.
     */
    private boolean boardIsFull() {
        for (int column = 0; column < BOARD_DIMENSION; column++) {
            if (board[column][0].equals(Cell.EMPTY_CELL))
                return false;
        }
        return true;
    }

    /**
     * Sets all the board's cells to empty. It's used once the once we create a new
     * game board.
     * 
     * @param board The created board.
     */
    private void setCellsToEmpty(Cell[][] board) {
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                board[i][j] = Cell.EMPTY_CELL;
            }
        }
    }

    /**
     * Switches between players after a player has finished their turn. E.g. if the
     * player one is now active it becomes the second and vice versa.
     */
    public void nextPlayer() {
        if (currentPlayer.equals(playerOne))
            currentPlayer = playerTwo;
        else if (currentPlayer.equals(playerTwo))
            currentPlayer = playerOne;
    }

    /**
     * @param columnNumber The input column number as a string.
     * @param rowNumber The input row number as a string.
     * @return The label of the token in the cell that is specified by the given coordinates.
     */
    public String getCellState(String columnNumber, String rowNumber) {
        int column = Integer.parseInt(columnNumber);
        int row = Integer.parseInt(rowNumber);
        return board[column][row].toString();
    }

    /**
     * Prints the actual appearance of the game board. The empty cells will appear
     * in this form {@code **}. The cells occupied by the first player will appear
     * in this form {@code P1}. The cells occupied by the second player will appear
     * in this form {@code P2}.
     */
    public void print() {
        for (int row = 0; row < BOARD_DIMENSION; row++) {
            String boardState = "";
            for (int column = 0; column < BOARD_DIMENSION; column++) {
                if (column == BOARD_DIMENSION - 1)
                    boardState += board[column][row].toString();
                else
                    boardState += board[column][row].toString() + " ";
            }
            Terminal.printLine(boardState);
        }
    }

    /**
     * @return The winner of the game.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * @return The current state of the game.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @return The current tokens number of the active player.
     */
    public int getTokensNumberOfCurrentPlayer() {
        return currentPlayer.getTokensNumber();
    }
}
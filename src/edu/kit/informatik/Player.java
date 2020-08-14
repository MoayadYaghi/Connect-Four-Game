package edu.kit.informatik;

import java.util.Objects;

/**
 * The player of Connect Four game.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public class Player {
    // Since the game obliges the first player to be named 'P1' and the second
    // player 'P2' we don't need a string variable for them and so we prevent the
    // names of being arbitrary.
    private final Cell label;
    private int tokensNumber;
    private int winningSequence;

    /**
     * Creates a player labeled {@code label}.
     * 
     * @param label The new player's label.
     * @param tokensNumber The new tokens' number.
     */
    public Player(Cell label, int tokensNumber) {
        this.label = label;
        this.tokensNumber = tokensNumber;
        winningSequence = 0;
    }

    /**
     * Reduces the tokens number of a player by one.
     */
    void reduceByOne() {
        tokensNumber--;
    }

    /**
     * Increases the number of the sequences of at least four tokens of the same
     * player by one.
     */
    void increaseByOne() {
        winningSequence++;
    }

    /**
     * Checks if the player still has tokens.
     * 
     * @return {@code true} If the player has tokens, {@code false} otherwise.
     */
    boolean hasTokens() {
        if (tokensNumber != 0)
            return true;
        return false;
    }

    /**
     * @return The label of the player.
     */
    public Cell getLabel() {
        return label;
    }

    /**
     * @return The current tokens number of a player.
     */
    int getTokensNumber() {
        return tokensNumber;
    }

    /**
     * @return The current number of winning-sequences of a player. A
     *         winning-sequence is a sequence of at least four tokens followed by
     *         each other of the same player in one direction (horizontal, vertical,
     *         diagonal).
     */
    int getWinningLine() {
        return winningSequence;
    }

    /**
     * Compares two players if they are the same. Two players are the same player if
     * they have the same label.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        if (this == object) {
            return true;
        }

        Player player = (Player) object;
        return Objects.equals(label, player.label);
    }
}
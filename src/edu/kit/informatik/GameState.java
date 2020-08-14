package edu.kit.informatik;

/**
 * The state of the Connect Four game.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public enum GameState {
    
    /**
     * While the game is being played.
     */
    RUNNING,
    
    /**
     * When the game is won by one of the players.
     */
    WON,
    
    /**
     * When either the game's board is full of tokens
     * or when the rival has no more tokens
     * or when both players won at the same time (after flip or remove command).
     */
    DRAW;
}
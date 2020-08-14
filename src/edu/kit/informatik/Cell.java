package edu.kit.informatik;

/**
 * The cell of the Connect Four game's board.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public enum Cell {

    /**
     * First player's token.
     */
    P1 {
        @Override
        public String toString() {
            return "P1";
        }
    },
    /**
     * Second player's token.
     */
    P2 {
        @Override
        public String toString() {
            return "P2";
        }
    },
    /**
     * No token in the cell.
     */
    EMPTY_CELL {
        @Override
        public String toString() {
            return "**";
        }
    };
}
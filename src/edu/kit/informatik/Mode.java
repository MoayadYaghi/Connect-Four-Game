package edu.kit.informatik;

/**
 * The made of Connect Four game. 
 *
 * @author Moayad Yaghi
 * @version 1.0
 */
public enum Mode {
    
    /**
     * The standard mode.
     */
    STANDARD {
        @Override
        public String toString() {
            return "standard";
        }
    },
    /**
     * The flip mode.
     */
    FLIP {
        @Override
        public String toString() {
            return "flip";
        }
    },
    /**
     * The remove mode.
     */
    REMOVE {
        @Override
        public String toString() {
            return "remove";
        }
    };
}
package edu.kit.informatik.exceptions;

/**
 * This exception is thrown if the the 'thrwoin' or 'remove' command is invalid.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public class IllegalCommandException extends Exception {
    
    /**
     * Creates an illegal command exception if the command was invalid.
     * 
     * @param errorMessage The error message to display to the user.
     */
    public IllegalCommandException(String errorMessage) {
        super(errorMessage);
    }
}
package edu.kit.informatik.exceptions;

/**
 * This exception is thrown if at least one of the command line arguments is
 * invalid.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public class IllegalArgumentException extends Exception {

    /**
     * Creates an illegal argument exception if the a command line argument is
     * invalid.
     * 
     * @param errorMessage
     *            The error message to display to the user.
     */
    public IllegalArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
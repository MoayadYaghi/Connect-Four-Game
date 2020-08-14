package edu.kit.informatik.UI;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exceptions.IllegalCommandException;

/**
 * The entrance to the program containing the main method.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public class Main {
    private static final int NUMBER_OF_ARGUMENTS = 2;

    /**
     * The main method of the program.
     * 
     * @param args The arguments passed to the program at its lunch as string.
     * @throws IllegalCommandException If at least one of the passed arguments is invalid.
     */
    public static void main(String[] args) throws IllegalCommandException {

        if (args.length < NUMBER_OF_ARGUMENTS) {
            Terminal.printError("not enough arguments, there needs to be two arguments.");
            return;
        } else if (args.length > NUMBER_OF_ARGUMENTS) {
            Terminal.printError("too many arguments, there needs to be only two arguments.");
            return;
        }

        try {
            GameManager connectFour = new GameManager(args[0], args[1]);
            connectFour.simulateGame();
        } catch (IllegalArgumentException e) {
            Terminal.printError(e.getMessage());
        }
    }
}
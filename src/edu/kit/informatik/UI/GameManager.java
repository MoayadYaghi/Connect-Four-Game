package edu.kit.informatik.UI;

import edu.kit.informatik.Cell;
import edu.kit.informatik.ConnectFourGame;
import edu.kit.informatik.GameState;
import edu.kit.informatik.Mode;
import edu.kit.informatik.Player;
import edu.kit.informatik.Terminal;
import edu.kit.informatik.exceptions.IllegalCommandException;

/**
 * This class controls the flow of the game.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public class GameManager {

    private ConnectFourGame myGame;
    private Mode mode;
    private String input;
    private boolean isRunning;

    /**
     * Creates a game manager with a game board with to players. A game must have
     * only two players and the must be 'P1' and 'P2'. Checks the input command line
     * arguments. Sets the command line arguments to the mode and the tokens number
     * for each player if they were valid.
     * 
     * 
     * @param mode The passed mode through the first command line argument.
     * @param tokens The passed tokens number through the second command line argument.
     * @throws IllegalArgumentException
     * If at least one of the passed command line arguments was invalid.
     */
    public GameManager(String mode, String tokens) throws IllegalArgumentException {
        if (!setGameMode(mode))
            throw new IllegalArgumentException("the first argument must be either 'standard', 'flip' or 'remove'.");
        else if (tokens.matches("(\\d\\d)")) {
            int tokensNumber = Integer.parseInt(tokens);
            Player playerOne = new Player(Cell.P1, tokensNumber);
            Player playerTwo = new Player(Cell.P2, tokensNumber);
            myGame = new ConnectFourGame(playerOne, playerTwo);
            checkTokensNumber(tokensNumber);
        } else {
            throw new IllegalArgumentException("the second argument must consist of a two-digit number.");
        }
    }

    /**
     * Runs the program to receive commands and output responses as long as the
     * program is running appropriately.
     * 
     * @throws IllegalCommandException If a 'throwin' or a 'remove' command was invalid.
     */
    public void simulateGame() throws IllegalCommandException {
        isRunning = true;
        while (isRunning) {
            input = Terminal.readLine();
            command(input);
        }
    }

    /**
     * Checks the input string if it's one of the following ("standard", "flip",
     * "remove"). If yes, it sets the string's corresponding mode to {@code mode},
     * otherwise it throws an exception.
     * 
     * @param mode The input game mode.
     * @throws IllegalArgumentException If the input string is not one valid modes ('standard', 'flip',
     *             'remove').
     */
    private boolean setGameMode(String mode) throws IllegalArgumentException {
        if (mode.equals(this.mode.STANDARD.toString())) {
            this.mode = Mode.STANDARD;
            return true;
        } else if (mode.equals(this.mode.FLIP.toString())) {
            this.mode = Mode.FLIP;
            return true;
        } else if (mode.equals(this.mode.REMOVE.toString())) {
            this.mode = Mode.REMOVE;
            return true;
        } else {
            isRunning = false;
            return false;
        }
    }
    
    /**
     * Checks the input number if it's between 27 and 33. If yes, it sets it to
     * {@code tokensNumber}, otherwise throws an exception.
     * 
     * @param tokensNumber Tokens number to be checked.
     * @throws IllegalArgumentException
     *             If the input number is not valid (among {28,32}).
     */
    private void checkTokensNumber(int tokensNumber) throws IllegalArgumentException {
        if (tokensNumber > myGame.MAXIMUM_TOKENS_NUMBER || tokensNumber < myGame.MINIMUM_TOKENS_NUMBER) {
            isRunning = false;
            throw new IllegalArgumentException("the number of tokens must be among {28,32}.");
        }
    }

    /**
     * Extracts the input column number within the remove or throw in commands and
     * returns it.
     * 
     * @param string Throw in or Remove command.
     * @return The extracted column number.
     */
    private int extractColumnNumber(String string) {
        String[] array = string.split("( )");
        int columnNumber = Integer.parseInt(array[1]);
        return columnNumber;
    }

    /**
     * Checks the validation of the input column number within a command. A column
     * number must only be from 0 to 7.
     * 
     * @param command The input command.
     * @return {@code true} if the column number is valid, {@code false} otherwise.
     */
    private boolean validColumnNumber(String command) {
        if (command.matches("([0-7])"))
            return true;
        Terminal.printError("invalid column number, please enter a number among {0,7}.");
        return false;
    }

    /**
     * Checks the validation of the input coordinates for the command state.
     * 
     * @param inputCoordinates
     *            The input coordinates as a string.
     * @return {@code true} if the coordinates match the form 'x;y', {@code false} otherwise.
     */
    private boolean checkCoordinates(String inputCoordinates) {
        if (!inputCoordinates.matches("([0-7];[0-7])")) {
            Terminal.printError("the coordinates must be in this form 'x;y'"
                    + " where x is the column number and y is the row number and they are both among {0,7}.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the number of arguments is valid for each command. The 'throwin',
     * 'remove' and 'state' commands have two arguments. All other commands must
     * consist of maximum one argument.
     * 
     * @return {@code true} if the passed command consists of valid number of
     *         arguments, {@code false} otherwise.
     */
    private boolean validArgumentsNumber(String[] commands) {
        if (commands[0].equals("throwin") || commands[0].equals("remove") || commands[0].equals("state")) {
            if (commands.length == 2)
                return true;
            else {
                Terminal.printError("this command must only consist of two arguments.");
                return false;
            }
        } else {
            if (commands.length == 1)
                return true;
            else {
                Terminal.printError("this command must only consist of one argument.");
                return false;
            }
        }

    }

    /**
     * Exits the program.
     */
    private void quit() {
        isRunning = false;
    }

    /**
     * Checks the validity of the commands 'throwin', 'flip' and 'remove' before
     * they are executed. If the {@code gameState} is {@code WON} or {@code DRAW}
     * the above mentioned commands are invalid. Also, if the {@code tokensNumber}
     * of the rival is zero then the mentioned commands are invalid.
     * 
     * @param command The input command.
     * @return {@code true} if the command is valid, {@code false} otherwise.
     */
    private boolean validCommand(String command) {
        if (myGame.getGameState() == GameState.WON) {
            Terminal.printError("the game is already won! You cannot use the '" + command + "' command anymore.");
            return false;
        } else if (myGame.getGameState() == GameState.DRAW) {
            Terminal.printError(
                    "the game is already finished with draw! You cannot use the '" + command + "' command anymore.");
            return false;
        }
        return true;
    }

    /**
     * Checks the state of the game and print the appropriate response according to
     * the state. This method is used for the three commands 'throwin', 'flip' and
     * 'remove'.
     */
    private void printResponse() {
        if (myGame.getGameState() == GameState.RUNNING)
            Terminal.printLine("OK");
        else if (myGame.getGameState() == GameState.WON)
            Terminal.printLine(myGame.getWinner().getLabel().toString() + " wins");
        else
            Terminal.printLine("draw");
    }

    /**
     * Executes the throwin command from the Connect Four Game class, checks the
     * game state and print the output using {@code printResponse} method. In case
     * the chosen column was full it catches the exception and prints it.
     * 
     * @param columnNumber The passed column number.
     */
    private void executeThrowin(int columnNumber) {
        try {
            if (myGame.throwin(columnNumber)) {
                myGame.checkGameState();
                printResponse();
            }
        } catch (IllegalCommandException e) {
            Terminal.printLine(e.getMessage());
        }
    }

    /**
     * Executes the flip command from the Connect Four Game class, switches the
     * players then checks the game state to print the proper output.
     */
    private void executeFlip() {
        myGame.flip();
        myGame.nextPlayer();
        myGame.checkGameState();
        printResponse();
    }

    /**
     * Executes the remove command from the Connect Four Game class, checks the game
     * state and print the output using {@code printResponse} method. In case the
     * chosen column was empty or the lower token of the chosen column belongs to
     * the rival it catches the exception and prints it.
     * 
     * @param columnNumber The passed column number.
     */
    private void executeRemove(int columnNumber) {
        try {
            if (myGame.remove(columnNumber)) {
                myGame.checkGameState();
                printResponse();
            }
        } catch (IllegalCommandException e) {
            Terminal.printLine(e.getMessage());
        }
    }
    
    /**
     * This method receives the input and processes it. If the command consists of
     * nothing or at least one white space it prints an error message. If the
     * command consists of at least one argument and the first argument is the first
     * part of one of the valid commands then it performs one to many checks using
     * methods from this class to the whole input command (first and second argument
     * if there is any) before it calls the actual corresponding method to the input
     * command. If the first argument of the input command was not a first part of
     * one of the valid commands it prints an error message. If the command is
     * invalid it uses methods from this class to print the corresponding error
     * message. The only valid commands are:
     * <ul>
     * <li>throwin x (x is the column number)
     * <li>flip
     * <li>remove x (x is the column number)
     * <li>token
     * <li>state x;y (x is the column number, y is the row number)
     * <li>print
     * <li>quit
     * </ul>
     * 
     * @param inputCommand The input command as a string.
     * @throws IllegalCommandException If a 'throwin' or a 'remove' command was invalid.
     */
    private void command(String inputCommand) throws IllegalCommandException {
        if (inputCommand.matches("(\\s*)"))
            Terminal.printError("your input is invalid, please input one of the valid commands.");
        else {
            String[] commands = inputCommand.split("( )");

            switch (commands[0]) {
                case "throwin": {
                    if (validArgumentsNumber(commands) && validCommand(commands[0]) && validColumnNumber(commands[1])) {
                        int columnNumber = extractColumnNumber(inputCommand);
                        executeThrowin(columnNumber);
                    }          
                }
                    break;

                case "flip": {
                    if (mode == Mode.REMOVE) {
                        Terminal.printError("you cannot use the flip command while the game's mode is 'remove'.");
                        break;
                    } else if (validArgumentsNumber(commands)) {
                        if (validCommand(commands[0]))
                            executeFlip();
                    }
                    break;
                }

                case "remove": {
                    if (mode == Mode.FLIP) {
                        Terminal.printError("you cannot use the remove command while the game's mode is 'flip'.");
                        break;
                    }
                    if (validArgumentsNumber(commands) && validCommand(commands[0]) && validColumnNumber(commands[1])) {
                        int columnNumber = extractColumnNumber(inputCommand);
                        executeRemove(columnNumber);
                    }
                }
                    break;

                case "token":
                    if (validArgumentsNumber(commands))
                        Terminal.printLine(myGame.getTokensNumberOfCurrentPlayer());
                    break;

                case "state": {
                    if (validArgumentsNumber(commands) && checkCoordinates(commands[1])) {
                        String[] coordinates = commands[1].split("(;)");
                        Terminal.printLine(myGame.getCellState(coordinates[0], coordinates[1]));
                    }  
                }
                    break;

                case "print":
                    if (validArgumentsNumber(commands))
                        myGame.print();
                    break;

                case "quit":
                    if (validArgumentsNumber(commands))
                        quit();
                    break;

                default:
                    Terminal.printError(
                            "the first entered argument of your input is invalid,"
                            + " please input one of the valid commands.");
            }
        }
    }
}
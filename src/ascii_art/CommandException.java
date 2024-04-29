package ascii_art;

/**
 * An exception that is thrown when the user provides an invalid command.
 * @see CommandException
 * Author: Ariel Pinhas and Amiel Wreschner
 */
abstract class CommandException extends Exception {
    /**
     * Constructs a CommandException object.
     * @param message the message to be displayed when the exception is thrown.
     */
    public CommandException(String message) {
        super(message);
    }
    /**
     * Constructs a CommandException object.
     */
    public CommandException() {
        super();
    }
}

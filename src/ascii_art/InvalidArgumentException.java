package ascii_art;

/**
 * An exception that is thrown when the user provides an invalid argument.
 * @see CommandException
 * Author: Ariel Pinhas and Amiel Wreschner
 */
public class InvalidArgumentException extends CommandException {
    /**
     * Constructs an InvalidArgumentException object.
     * @param message the message to be displayed when the exception is thrown
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}

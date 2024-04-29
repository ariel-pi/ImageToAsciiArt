package ascii_art;

/**
 * An exception that is thrown when the user provides an invalid command.
 * @see CommandException
 * Author: Ariel Pinhas and Amiel Wreschner
 */

public class ExceedingBoundariesException extends CommandException{
    /**
     * Constructs an ExceedingBoundariesException object.
     */
    public ExceedingBoundariesException() {
        super();
    }
}

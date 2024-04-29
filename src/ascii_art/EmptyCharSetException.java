package ascii_art;

/**
 * An exception that is thrown when the user provides an empty char set.
 * @see CommandException
 * Author: Ariel Pinhas and Amiel Wreschner
 */
public class EmptyCharSetException extends CommandException{
    /**
     * Constructs an EmptyCharSetException object.
     */
    public EmptyCharSetException() {
        super();
    }
}

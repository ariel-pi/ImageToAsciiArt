package ascii_art;

/**
 * An exception that is thrown when the user provides an invalid image argument.
 * @see CommandException
 * Author: Ariel Pinhas and Amiel Wreschner
 */
public class InvalidImageArgumentException  extends CommandException{

    /**
     * Constructs an InvalidImageArgumentException object.
     */
    public InvalidImageArgumentException() {
        super();
    }
}

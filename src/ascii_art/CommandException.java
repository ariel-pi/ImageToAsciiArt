package ascii_art;

abstract class CommandException extends Exception {
    public CommandException(String message) {
        super(message);
    }
    public CommandException() {
        super();
    }
}

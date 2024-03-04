package ascii_art;

import java.util.TreeSet;

public class Shell {

    private TreeSet<Character> asciiCharsToUse;

    public void run() {
        this.asciiCharsToUse = new TreeSet<>();
        System.out.print(">>>");
        String input = KeyboardInput.readLine();
        while (!input.equals("exit")) {
            System.out.println("You entered: " + input);
            System.out.print(">>>");
            input = KeyboardInput.readLine();
        }
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }
}

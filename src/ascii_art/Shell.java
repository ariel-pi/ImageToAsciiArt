package ascii_art;

import ascii_output.ConsoleAsciiOutput;

import java.io.IOException;
import java.util.TreeSet;

public class Shell {

    public static final char[] DEFAULT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private TreeSet<Character> asciiCharsToUse;
    private AsciiArtAlgorithm asciiArtAlgorithm;

    public void run() throws IOException { //todo: remove throws IOException and to add try-catch
        initializeDefaultValues();
        System.out.print(">>>");
        String input = KeyboardInput.readLine();
        while (!input.equals("exit")) {
            handleInput(input);
            System.out.print(">>>");
            input = KeyboardInput.readLine();
        }
    }

    private void initializeDefaultValues() throws IOException {
        asciiArtAlgorithm = new AsciiArtAlgorithm("cat.jpeg", DEFAULT_CHARS);
        this.asciiCharsToUse = new TreeSet<>();
        for (char c : DEFAULT_CHARS) {
            this.asciiCharsToUse.add(c);
            this.asciiArtAlgorithm.addChar(c);
        }
    }

    private void handleInput(String input) throws IOException {
        //todo: add try-catch
        String[] inputArr = input.split(" ");
        switch (inputArr[0]) {
            case "chars":
                printChars();
                break;
            case "add":
                addChar(inputArr[1]);
                break;
            case "remove":
                removeChar(inputArr[1]);
                break;
            case "res":
                updateResolution(inputArr[1]);
                break;
            case "image": //todo why not working?
                asciiArtAlgorithm.setNewImage(inputArr[1]);
                break;
            case "asciiArt":
                char[][] asciiArt = asciiArtAlgorithm.run();
                ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
                consoleAsciiOutput.out(asciiArt);
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    private void updateResolution(String changeResolutionCommand) {
        try {
            if (changeResolutionCommand.equals("up")) {
                this.asciiArtAlgorithm.resolutionUp();
            } else if (changeResolutionCommand.equals("down")) {
                this.asciiArtAlgorithm.resolutionDown();
            } else {
                System.out.println("Did not change resolution due to incorrect format.");
            }
            System.out.println("Resolution set to " + this.asciiArtAlgorithm.getResolution());
        }
        //todo: change to more appropriate exception
        catch (IllegalArgumentException e){
            System.out.println("Did not change resolution due to exceeding boundaries.");
        }

    }

    private void removeChar(String charsToRemove) {
        char[] newChars = handleChars(charsToRemove);
        for (char c : newChars) {
            this.asciiCharsToUse.remove(c);
            asciiArtAlgorithm.removeChar(c);
        }
    }

    private void addChar(String charsToAdd) {
        char[] newChars = handleChars(charsToAdd);
        for (char c : newChars) {
            this.asciiCharsToUse.add(c);
            asciiArtAlgorithm.addChar(c);
        }
    }

    private char[] handleChars(String charsToAdd) {
        char[] charsToHandle = new char[0];
        if (charsToAdd.equals("all")) {
            charsToHandle = new char[127 - 32];
            for (int i = 32; i < 127; i++) {
                charsToHandle[i - 32] = (char) i;
            }
        } else if (charsToAdd.equals("space")) {
            charsToHandle = new char[1];
            charsToHandle[0] = ' ';
        } else if (charsToAdd.length() == 1) {
            charsToHandle = new char[1];
            charsToHandle[0] = charsToAdd.charAt(0);
        } else if (charsToAdd.charAt(1) == '-') {

            char start = charsToAdd.charAt(0);
            char end = charsToAdd.charAt(2);
            if (start > end) {
                char temp = start;
                start = end;
                end = temp;
            }
            charsToHandle = new char[end - start + 1];
            for (int i = start; i <= end; i++) {
                charsToHandle[i - start] = (char) i;
            }
        }
        else {
            assert false : "Invalid charsToAdd";//todo: remove assert
        }
        return charsToHandle;
    }

    private void printChars() {
        for (char c : this.asciiCharsToUse) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        Shell shell = new Shell();
        shell.run();
    }
}

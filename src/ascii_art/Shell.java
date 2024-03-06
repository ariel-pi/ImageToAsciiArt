package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.TreeSet;

public class Shell {

    public static final char[] DEFAULT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final int DEFAULT_RESOLUTION = 128;
    public static final String DEFAULT_PATH = "cat.jpeg";
    private int resolution;
    private int maxResolution;
    private int minResolution;
    private TreeSet<Character> asciiCharsToUse;
    private AsciiArtAlgorithm asciiArtAlgorithm;
    private char[][] asciiArt;
    private boolean isNeedToUpdateAsciiArt = true;
    private boolean isNeedToCalculateBrightness = true;
    private AsciiOutput asciiOutput = new ConsoleAsciiOutput();
    private SubImgCharMatcher subImgCharMatcher;
    private Image image;

    public void run() throws IOException { //todo: remove throws IOException and to add try-catch
        initializeDefaultValues();
        System.out.print(">>> ");
        String input = KeyboardInput.readLine();
        while (!input.equals("exit")) {
            handleInput(input);
            System.out.print(">>> ");
            input = KeyboardInput.readLine();
        }
    }

    private void initializeDefaultValues() throws IOException {
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARS);
        resolution = DEFAULT_RESOLUTION;
        setNewImage(DEFAULT_PATH);
        this.asciiCharsToUse = new TreeSet<>();
        for (char c : DEFAULT_CHARS) {
            this.asciiCharsToUse.add(c);
            this.subImgCharMatcher.addChar(c);
        }
    }

    private void handleInput(String input) throws IOException {
        //todo: add try-catch
        String[] inputArr = input.split(" ");
        switch (inputArr[0]) {
            //todo if inputArr[1] is not exist
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
            case "image":
                setNewImage(inputArr[1]);
                break;
            case "output":
                handleOutput(inputArr[1]);
                break;
            case "asciiArt": //todo didnt succed to do it afficiently
                if(isNeedToUpdateAsciiArt){
                    if (isNeedToCalculateBrightness) {
                        asciiArtAlgorithm = new AsciiArtAlgorithm(image, subImgCharMatcher, resolution, true);
                        asciiArt = asciiArtAlgorithm.run();
                        isNeedToCalculateBrightness = false;
                        System.out.println("in new AsciiArtAlgorithm");
                    }
                    else {
                        asciiArt = asciiArtAlgorithm.run();
                        System.out.println("in just set asciiArt");
                    }
                    isNeedToUpdateAsciiArt = false;
                }
                asciiOutput.out(asciiArt);
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    private void setNewImage(String path) throws IOException {
        image = new Image(path);
        image = ImageProcessor.padImage(image);
        maxResolution = image.getWidth();
        minResolution = Math.max(1, image.getWidth() / image.getHeight());
        isNeedToUpdateAsciiArt = true;
        isNeedToCalculateBrightness = true;
    }

    private void handleOutput(String outputType) {
        switch (outputType) {
            case "html":
                asciiOutput = new ascii_output.HtmlAsciiOutput("output.html", "Courier New");
                break;
            case "console":
                asciiOutput = new ConsoleAsciiOutput();
                break;
            default:
                System.out.println("did not change output method due to incorrect format");
        }
    }

    //todo: change to more appropriate exception
    private void updateResolution (String changeResolutionCommand)  throws IllegalArgumentException {
            if (changeResolutionCommand.equals("up")) {
                resolutionUp();
            } else if (changeResolutionCommand.equals("down")) {
                resolutionDown();
            } else { //todo delete and throw exception instead
                System.out.println("Did not change resolution due to incorrect format.");
                return;
            }
            System.out.println("Resolution set to " + resolution);


    }

    private void resolutionUp() {
        if (resolution < maxResolution) {
            resolution *= 2;
            isNeedToUpdateAsciiArt = true;
            isNeedToCalculateBrightness = true;
        }
        else{
            // todo: change to more appropriate exception
            throw new IllegalArgumentException("Resolution is already at maximum");
        }
    }
    private void resolutionDown() {
        if (resolution > this.minResolution) {
            resolution /= 2;
            isNeedToUpdateAsciiArt = true;
            isNeedToCalculateBrightness = true;

        }
        else{
            //todo: change to more appropriate exception
            throw new IllegalArgumentException("Resolution is already at minimum");
        }
    }

    private void removeChar(String charsToRemove) {
        char[] newChars = handleChars(charsToRemove);
        for (char c : newChars) {
            this.asciiCharsToUse.remove(c);
            subImgCharMatcher.removeChar(c);
        }
        isNeedToUpdateAsciiArt = true;
    }

    private void addChar(String charsToAdd) {
        char[] newChars = handleChars(charsToAdd);
        for (char c : newChars) {
            this.asciiCharsToUse.add(c);
            subImgCharMatcher.addChar(c);
        }
        isNeedToUpdateAsciiArt = true;
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

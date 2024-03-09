package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Shell {
    public static final String EXCEEDING_BOUNDARIES_MASSAGE = "Did not" +
            " change resolution due to exceeding boundaries.";
    public static final String INCORRECT_COMMAND_MASSAGE = "Did not execute due to incorrect command.";
    private static final String IMAGE_LOAD_ERROR_MASSAGE = "Did not execute due to problem with image file.";
    public static final String EMPTY_CHARSET_ERROR_MASSAGE = "Did not execute. Charset is empty.";




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

    public void run() {
        try {
            initializeDefaultValues();

        }
        catch (InvalidImageArgumentException e){
            System.out.println(IMAGE_LOAD_ERROR_MASSAGE);
        }
        System.out.print(">>> ");
        String input = KeyboardInput.readLine();
        while (!input.equals("exit")) {
            try{
                handleInput(input);
            }
            catch (InvalidArgumentException e){
                System.out.println("Did not "+e.getMessage()+" due to incorrect format.");
            }
            catch (ExceedingBoundariesException e){
                System.out.println(EXCEEDING_BOUNDARIES_MASSAGE);
            }
            catch (InvalidImageArgumentException e){
                System.out.println(IMAGE_LOAD_ERROR_MASSAGE);
            }
            catch (EmptyCharSetException e){
                System.out.println(EMPTY_CHARSET_ERROR_MASSAGE);
            }
            catch (InvalidCommandException e) {
                System.out.println(INCORRECT_COMMAND_MASSAGE);
            }
            catch (CommandException e) {
                System.out.println(INCORRECT_COMMAND_MASSAGE);
            }


            System.out.print(">>> ");
            input = KeyboardInput.readLine();
        }
    }




    private void initializeDefaultValues() throws InvalidImageArgumentException{
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARS);
        resolution = DEFAULT_RESOLUTION;
        setNewImage(DEFAULT_PATH);
        this.asciiCharsToUse = new TreeSet<>();
        for (char c : DEFAULT_CHARS) {
            this.asciiCharsToUse.add(c);
            this.subImgCharMatcher.addChar(c);
        }
    }

    private void handleInput(String input) throws CommandException {
        String[] inputArr = input.split(" ");
        switch (inputArr[0]) {
            //if inputArr[1] is not exist, and should exist, we will throw an exception, although it is not required.
            case "chars":
                printChars();
                break;
            case "add":
                if(!isArgsExist(inputArr)){
                    throw new InvalidArgumentException("add");
                }
                addChar(inputArr[1]);
                break;
            case "remove":
                if(!isArgsExist(inputArr)){
                    throw new InvalidArgumentException("remove");
                }
                removeChar(inputArr[1]);
                break;
            case "res":
                if(!isArgsExist(inputArr)){
                    throw new InvalidArgumentException("change resolution");
                }
                updateResolution(inputArr[1]);
                break;
            case "image":
                if(!isArgsExist(inputArr)){
                    throw new InvalidImageArgumentException();
                }
                setNewImage(inputArr[1]);
                break;
            case "output":
                if(!isArgsExist(inputArr)){
                    throw new InvalidArgumentException("change output method");
                }
                handleOutput(inputArr[1]);
                break;
            case "asciiArt":
                executeAsciiArt();
                break;
            default:
                throw new InvalidCommandException();
        }
    }

    private boolean isArgsExist(String[] inputArr) {
        return inputArr.length >= 2;

    }

    private void executeAsciiArt() throws EmptyCharSetException {
        if(isNeedToUpdateAsciiArt){
            if (isNeedToCalculateBrightness) {
                asciiArtAlgorithm = new AsciiArtAlgorithm(image, subImgCharMatcher, resolution);
                asciiArt = asciiArtAlgorithm.run();
                isNeedToCalculateBrightness = false;
            }
            else {
                asciiArt = asciiArtAlgorithm.run();
            }
            isNeedToUpdateAsciiArt = false;
        }
        asciiOutput.out(asciiArt);
    }

    private void setNewImage(String path) throws InvalidImageArgumentException {
        try {
            image = new Image(path);
        }
        catch (IOException e){
            throw new InvalidImageArgumentException();
        }
        image = ImageProcessor.padImage(image);
        maxResolution = image.getWidth();
        minResolution = Math.max(1, image.getWidth() / image.getHeight());
        isNeedToUpdateAsciiArt = true;
        isNeedToCalculateBrightness = true;
    }

    private void handleOutput(String outputType) throws InvalidArgumentException{
        switch (outputType) {
            case "html":
                asciiOutput = new ascii_output.HtmlAsciiOutput("output.html", "Courier New");
                break;
            case "console":
                asciiOutput = new ConsoleAsciiOutput();
                break;
            default:
                throw new InvalidArgumentException("change output method");
        }
    }

    private void updateResolution (String changeResolutionCommand)  throws CommandException {
            if (changeResolutionCommand.equals("up")) {
                resolutionUp();
            } else if (changeResolutionCommand.equals("down")) {
                resolutionDown();
            } else {
                throw new InvalidArgumentException("change resolution");

            }


            System.out.println("Resolution set to " + resolution);


    }

    private void resolutionUp() throws ExceedingBoundariesException {
        if (resolution < maxResolution) {
            resolution *= 2;
            isNeedToUpdateAsciiArt = true;
            isNeedToCalculateBrightness = true;
        }
        else{
            throw new ExceedingBoundariesException();
        }
    }
    private void resolutionDown() throws ExceedingBoundariesException {
        if (resolution > this.minResolution) {
            resolution /= 2;
            isNeedToUpdateAsciiArt = true;
            isNeedToCalculateBrightness = true;

        }
        else{
            throw new ExceedingBoundariesException();
        }
    }

    private void removeChar(String charsToRemove) throws InvalidArgumentException{
        char[] newChars = handleChars(charsToRemove);
        if (newChars.length == 0) {
            throw new InvalidArgumentException("remove");
        }
        for (char c : newChars) {
            this.asciiCharsToUse.remove(c);
            subImgCharMatcher.removeChar(c);
        }
        isNeedToUpdateAsciiArt = true;
    }

    private void addChar(String charsToAdd) throws InvalidArgumentException {

        char[] newChars = handleChars(charsToAdd);
        if (newChars.length == 0) {
            throw new InvalidArgumentException("add");
        }
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
        } else if (charsToAdd.charAt(1) == '-' && charsToAdd.length() == 3){

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

        return charsToHandle;
    }

    private void printChars() {
        for (char c : this.asciiCharsToUse) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    public static void main(String[] args){
        Shell shell = new Shell();
        shell.run();
    }

}

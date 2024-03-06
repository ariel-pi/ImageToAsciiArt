package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class AsciiArtAlgorithm {

    private Image image;
    private Image[][] subImages;
    private int resolution=128;
    private SubImgCharMatcher subImgCharMatcher;
    private char[][] asciiArt;
    private int maxResolution;
    private int minResolution;

    public AsciiArtAlgorithm(String filename, char[] asciiChars) throws IOException {
        // Load the image from the file.
        this.image = new Image(filename);
        this.subImgCharMatcher = new SubImgCharMatcher(asciiChars);

    }

    public char[][] run() {
        // Pad the image to a power of 2.
        image = ImageProcessor.padImage(image);
        this.maxResolution = image.getWidth();
        this.minResolution = Math.max(1, image.getWidth() / image.getHeight());
        // Divide the image into sub-images.
        this.subImages = ImageProcessor.divideIntoSubImages(image, resolution);
        asciiArt = setAsciiArt();
        return asciiArt;
    }

    private char[][] setAsciiArt() {
        asciiArt = new char[subImages.length][subImages[0].length];
        subImgCharMatcher.normalizeBrightness();
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                double imageBrightness = ImageProcessor.convertToGrayScale(subImages[i][j]);
                asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(imageBrightness);
            }
        }
        return asciiArt;
    }

    //todo: remove the following methods
    public static void main(String[] args) throws IOException {
        int startAsciiValue = 32; // Starting ASCII value
        int numChars = 127 - startAsciiValue; // Number of characters to include (ASCII values 32 to 127)

        char[] ascii = new char[numChars];

        for (int i = startAsciiValue; i < 127; i++) {
            ascii[i - startAsciiValue] = (char) i;
        }
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm("examples/WhatsApp Image 2024-03-04 at 13.07.25.jpeg",
                ascii);
        char[][] asciiArt = asciiArtAlgorithm.run();
//        ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
//        consoleAsciiOutput.out(asciiArt);
        HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput("examples/twins.html", "Courier New");
        htmlAsciiOutput.out(asciiArt);
    }

    public void addChar(char c) {
        subImgCharMatcher.addChar(c);
    }

    public void removeChar(char c) {
        subImgCharMatcher.removeChar(c);
    }

    public void resolutionUp() {
        if (resolution < maxResolution) {
            resolution *= 2;
        }
        else{
            // todo: change to more appropriate exception
            throw new IllegalArgumentException("Resolution is already at maximum");
        }
    }
    public void resolutionDown() {
        if (resolution > this.minResolution) {
            resolution /= 2;
        }
        else{
            //todo: change to more appropriate exception
            throw new IllegalArgumentException("Resolution is already at minimum");
        }
    }

    public int getResolution() {
        return resolution;
    }


    public void setNewImage(String newImagePath) throws IOException {
        this.image = new Image(newImagePath);
        this.subImages = ImageProcessor.divideIntoSubImages(image, resolution);
        setAsciiArt();

    }
}

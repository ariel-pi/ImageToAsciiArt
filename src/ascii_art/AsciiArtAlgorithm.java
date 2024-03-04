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
    private int resolution;
    private SubImgCharMatcher subImgCharMatcher;
    private char[][] asciiArt;

    public AsciiArtAlgorithm(String filename, int resolution, char[] asciiChars) throws IOException {
        // Load the image from the file.
        this.image = new Image(filename);
        this.resolution = resolution;
        this.subImgCharMatcher = new SubImgCharMatcher(asciiChars);
    }

    public char[][] run() {
        // Pad the image to a power of 2.
        image = ImageProcessor.padImage(image);
        // Divide the image into sub-images.
        this.subImages = ImageProcessor.divideIntoSubImages(image, resolution);
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

    public static void main(String[] args) throws IOException {
        int startAsciiValue = 32; // Starting ASCII value
        int numChars = 127 - startAsciiValue; // Number of characters to include (ASCII values 32 to 127)

        char[] ascii = new char[numChars];

        for (int i = startAsciiValue; i < 127; i++) {
            ascii[i - startAsciiValue] = (char) i;
        }
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm("examples/WhatsApp Image 2024-03-04 at 13.07.25.jpeg", 256,
                ascii);
        char[][] asciiArt = asciiArtAlgorithm.run();
//        ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
//        consoleAsciiOutput.out(asciiArt);
        HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput("examples/twins.html", "Courier New");
        htmlAsciiOutput.out(asciiArt);
    }
}

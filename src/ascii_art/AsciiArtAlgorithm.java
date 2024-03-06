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

    private final Image image;
    private Image[][] subImages;
    private double[][] brightnessMap;
    private final SubImgCharMatcher subImgCharMatcher;
    private char[][] asciiArt;
    private final int resolution;
    private boolean isNeedToCalculateBrightness;

    public AsciiArtAlgorithm(
            Image image,
            SubImgCharMatcher subImgCharMatcher,
            int resolution, boolean isNeedToCalculateBrightness) throws IOException {
        this.image = image;
        this.isNeedToCalculateBrightness = isNeedToCalculateBrightness;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;

    }

    public char[][] run() {
        this.subImages = ImageProcessor.divideIntoSubImages(image, resolution);
        setAsciiArt();
        return asciiArt;
    }

    private void setAsciiArt() {
        asciiArt = new char[subImages.length][subImages[0].length];
        brightnessMap = new double[subImages.length][subImages[0].length];
        subImgCharMatcher.normalizeBrightness();
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                double imageBrightness;
                if (isNeedToCalculateBrightness) {
                     imageBrightness = ImageProcessor.convertToGrayScale(subImages[i][j]);
                     brightnessMap[i][j] = imageBrightness;
                     asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(imageBrightness);
                    System.out.print("calc");
                }
                else {
                    imageBrightness = brightnessMap[i][j];
                    asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(imageBrightness);
                }
            }
        }
    }

}

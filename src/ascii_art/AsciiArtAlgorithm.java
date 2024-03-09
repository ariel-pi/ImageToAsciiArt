package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class AsciiArtAlgorithm {

    private final Image image;
    private SubImage[][] subImages;
    private final SubImgCharMatcher subImgCharMatcher;
    private char[][] asciiArt;
    private final int resolution;

    public AsciiArtAlgorithm(
            Image image,
            SubImgCharMatcher subImgCharMatcher,
            int resolution) throws IOException {
        this.image = image;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
        this.subImages = ImageProcessor.divideIntoSubImages(image, resolution);

    }

    public char[][] run() {
        setAsciiArt();
        return asciiArt;
    }

    private void setAsciiArt() {
        asciiArt = new char[subImages.length][subImages[0].length];
        System.out.println("calc");
        for (int i = 0; i < subImages.length; i++) {
                for (int j = 0; j < subImages[i].length; j++) {
                    if (subImages[i][j].getBrightness() == SubImage.NULL_BRIGHTNESS) {
                        subImages[i][j].setBrightness(ImageProcessor.convertToGrayScale(subImages[i][j]));
                    }
                    asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(subImages[i][j].getBrightness());


                }
            }

    }

}

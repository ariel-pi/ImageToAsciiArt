package ascii_art;

import image.Image;
import image.ImageProcessor;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

/**
 * An object implementing this interface can output a 2D array of chars
 * in some fashion.
 * @see Image
 * @see SubImage
 * @see SubImgCharMatcher
 * @see ImageProcessor
 * @see EmptyCharSetException
 *
 * Author: Ariel Pinhas and Amiel Wreschner
 */
public class AsciiArtAlgorithm {

    private final Image image;
    private SubImage[][] subImages;
    private final SubImgCharMatcher subImgCharMatcher;
    private char[][] asciiArt;
    private final int resolution;

    /**
     * Constructs an AsciiArtAlgorithm object.
     * @param image the image to be converted to ascii art
     * @param subImgCharMatcher the object that will match the sub images to chars
     * @param resolution the resolution of the ascii art
     */
    public AsciiArtAlgorithm(
            Image image,
            SubImgCharMatcher subImgCharMatcher,
            int resolution) {
        this.image = image;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
        this.subImages = ImageProcessor.divideIntoSubImages(image, resolution);

    }

    /**
     * Runs the algorithm and returns the ascii art.
     * @return the ascii art
     * @throws EmptyCharSetException if the char set is empty
     */
    public char[][] run() throws EmptyCharSetException {
        setAsciiArt();
        return asciiArt;
    }

    private void setAsciiArt() throws EmptyCharSetException {
        asciiArt = new char[subImages.length][subImages[0].length];
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                if (subImages[i][j].getBrightness() == SubImage.NULL_BRIGHTNESS) {
                    subImages[i][j].setBrightness(ImageProcessor.convertToGrayScale(subImages[i][j]));
                }
                asciiArt[i][j] = subImgCharMatcher.
                        getCharByImageBrightness(subImages[i][j].getBrightness());

            }
        }

    }

}

package ascii_art;

import image.Image;
import image.ImageProcessor;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

public class AsciiArtAlgorithm {

    private final Image image;
    private SubImage[][] subImages;
    private final SubImgCharMatcher subImgCharMatcher;
    private char[][] asciiArt;
    private final int resolution;

    public AsciiArtAlgorithm(
            Image image,
            SubImgCharMatcher subImgCharMatcher,
            int resolution) {
        this.image = image;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
        this.subImages = ImageProcessor.divideIntoSubImages(image, resolution);

    }

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
                    asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(subImages[i][j].getBrightness());

                }
            }

    }

}

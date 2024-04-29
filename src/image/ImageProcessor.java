package image;

import java.awt.*;
import java.util.Arrays;

/**
 * Provides various image processing operations.
 * This class contains static methods for processing images, such as padding, dividing into sub-images,
 * converting to grayscale, etc.
 * @see Image
 * @see SubImage
 *
 * Author: Ariel Pinhas, Amiel Wreschner
 */
public class ImageProcessor {

    /**
     * Pads the given image to make its dimensions a power of 2.
     * If the dimensions are already powers of 2, returns the original image.
     * @param image The image to pad.
     * @return The padded image.
     */
    public static Image padImage(Image image) {
        int currentHeight = image.getHeight();
        int currentWidth = image.getWidth();
        int newHeight = getCeilingPowOf2(currentHeight);
        int newWidth = getCeilingPowOf2(currentWidth);
        if (newHeight == currentHeight && newWidth == currentWidth) {
            return image;
        }
        int paddingHeight = (newHeight - currentHeight) / 2;
        int paddingWidth = (newWidth - currentWidth) / 2;
        Color[][] paddedImagePixels = new Color[newHeight][newWidth];
        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                if (row < paddingHeight || row >= paddingHeight + currentHeight ||
                        col < paddingWidth || col >= paddingWidth + currentWidth) {
                    paddedImagePixels[row][col] = Color.WHITE;
                } else {
                    paddedImagePixels[row][col] = image.getPixel(row - paddingHeight, col - paddingWidth);
                }
            }
        }
        return new Image(paddedImagePixels, newWidth, newHeight);
    }

    /**
     * Divides the padded image into a grid of sub-images.
     * @param paddedImage The padded image to divide.
     * @param numOfCols The number of columns in the grid.
     * @return A 2D array of SubImage objects representing the divided sub-images.
     */
    public static SubImage[][] divideIntoSubImages(Image paddedImage, int numOfCols) {
        int height = paddedImage.getHeight();
        int width = paddedImage.getWidth();
        int sizeOfSubImg = width / numOfCols;
        int numOfRows = height / sizeOfSubImg;
        SubImage[][] subImages = new SubImage[numOfRows][numOfCols];
        Color[][] paddedImagePixels = paddedImage.getPixelArray();
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                Color[][] subImagePixels = new Color[sizeOfSubImg][sizeOfSubImg];
                for (int k = 0; k < sizeOfSubImg; k++) {
                    subImagePixels[k] = Arrays.copyOfRange(paddedImagePixels[i * sizeOfSubImg + k],
                            j * sizeOfSubImg,
                            (j + 1) * sizeOfSubImg);
                }

                Image image = new Image(subImagePixels, sizeOfSubImg, sizeOfSubImg);

                subImages[i][j] = new SubImage(image);

            }
        }
        return subImages;
    }
    /**
     * Converts a sub-image to grayscale and calculates its average grayscale value.
     * @param subimage The sub-image to convert and calculate grayscale for.
     * @return The average grayscale value of the sub-image.
     */
    public static double convertToGrayScale(SubImage subimage) {
        double sum = 0;
        for (int i = 0; i < subimage.getImage().getHeight(); i++) {
            for (int j = 0; j < subimage.getImage().getWidth(); j++) {
                sum += getPixelGrayScale(subimage.getImage().getPixel(i, j));
            }
        }
        return  (sum / (subimage.getImage().getHeight() * subimage.getImage().getWidth()));
    }

    /**
     * Calculates the grayscale value of a pixel using the luminosity method.
     * @param color The color of the pixel.
     * @return The grayscale value of the pixel.
     */
    private static double getPixelGrayScale(Color color) {
        return (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue()) / 255;
    }

    /**
     * Calculates the smallest power of 2 greater than or equal to the given value.
     * @param currentHeight The current value.
     * @return The smallest power of 2 greater than or equal to the given value.
     */
    private static int getCeilingPowOf2(int currentHeight) {
        int powerOf2 = 1;
        while (powerOf2 < currentHeight) {
            powerOf2 *= 2;
        }
        return powerOf2;
    }
}

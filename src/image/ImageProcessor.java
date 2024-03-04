package image;

import java.awt.*;
import java.util.Arrays;

public class ImageProcessor {


    public static Color[][] padImage(Color[][] pixelArray) {
        int currentHeight = pixelArray.length;
        int currentWidth = pixelArray[0].length;
        int newHeight = getCeilingPowOf2(currentHeight);
        int newWidth = getCeilingPowOf2(currentWidth);
        if (newHeight == currentHeight && newWidth == currentWidth) {
            return pixelArray;
        }
        int paddingHeight = (newHeight - currentHeight) / 2;
        int paddingWidth = (newWidth - currentWidth) / 2;
        Color[][] paddedImage = new Color[newHeight][newWidth];
        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                if (row < paddingHeight || row >= paddingHeight + currentHeight ||
                        col < paddingWidth || col >= paddingWidth + currentWidth) {
                    paddedImage[row][col] = Color.WHITE;
                } else {
                    paddedImage[row][col] = pixelArray[row - paddingHeight][col - paddingWidth];
                }
            }
        }
        return paddedImage;
    }

    private static int getCeilingPowOf2(int currentHeight) {
        int powerOf2 = 1;
        while (powerOf2 < currentHeight) {
            powerOf2 *= 2;
        }
        return powerOf2;
    }

    public static Image[][] divideIntoSubImages(Color[][] paddedImage, int numOfCols) {
        int height = paddedImage.length;
        int width = paddedImage[0].length;
        int sizeOfSubImg = width / numOfCols;
        int numOfRows = height / sizeOfSubImg;
        Image[][] subImages = new Image[numOfRows][numOfCols];
        // todo: change the logic so we will iterate by subimages and not by pixels, and then we will not
        //  need setPixel method

        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                Color[][] subImagePixels = new Color[sizeOfSubImg][sizeOfSubImg];
                for (int k = 0; k < sizeOfSubImg; k++) {
                    subImagePixels[k] = Arrays.copyOfRange(paddedImage[i * sizeOfSubImg + k], j * sizeOfSubImg,
                            (j + 1) * sizeOfSubImg);
                }
                subImages[i][j] = new Image(subImagePixels, sizeOfSubImg, sizeOfSubImg);
            }
        }
        return subImages;
    }

    public static double convertToGrayScale(Image image) {
        double sum = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                sum += getPixelGrayScale(image.getPixel(i, j));
            }
        }
        return  sum / (image.getHeight() * image.getWidth());
    }

    public static double getPixelGrayScale(Color color) {
        return 0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue();
    }

}
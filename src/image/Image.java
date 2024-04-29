package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents an image composed of pixels.
 * This class provides functionality to create, manipulate, and save images.
 *
 * Author:  Ariel Pinhas, Amiel Wreschner
 */
public class Image {

    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Constructs an Image object from a file.
     * @param filename The path to the image file.
     * @throws IOException if an error occurs while reading the image file.
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();
        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j] = new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * Constructs an Image object from a pixel array.
     * @param pixelArray The array of Color objects representing the pixels.
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the width of the image.
     * @return The width of the image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the image.
     * @return The height of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the Color of the pixel at the specified coordinates.
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @return The Color of the pixel at the specified coordinates.
     */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Saves the image to a file.
     * @param fileName The name of the file to save the image to.
     */
    public void saveImage(String fileName) {
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName + ".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the pixel array of the image.
     * @return The pixel array of the image.
     */
    public Color[][] getPixelArray() {
        return pixelArray;
    }
}

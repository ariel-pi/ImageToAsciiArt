package ascii_art;

import image.Image;

import java.io.IOException;

public class AsciiArtAlgorithm {

    public AsciiArtAlgorithm(String filename, int resolution, char[] asciiChars) throws IOException {
        // Load the image from the file.
        Image image = new Image(filename);
        // Create a 2D array of chars to store the ASCII art.

        // For each pixel in the image, convert it to a grayscale value.
        // Map the grayscale value to an ASCII character.
        // Store the ASCII character in the 2D array.
        // Output the 2D array of ASCII characters.
    }
}

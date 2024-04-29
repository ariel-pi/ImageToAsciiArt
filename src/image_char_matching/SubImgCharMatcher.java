package image_char_matching;

import ascii_art.EmptyCharSetException;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Matches characters to image brightness levels.
 * This class provides functionality to match characters to image brightness levels
 * based on a predefined character set.
 *
 * Author: Ariel Pinhas, Amiel Wreschner
 */
public class SubImgCharMatcher {

    private TreeMap<Double, TreeSet<Character>> charBrightnessMap = new TreeMap<>();
    private Double maxBrightness;
    private Double minBrightness;

    /**
     * Constructs a SubImgCharMatcher with a given character set.
     * @param charset The character set used for matching.
     */
    public SubImgCharMatcher(char[] charset) {
        maxBrightness = null;
        minBrightness = null;
        for (char c : charset) {
            addChar(c);
        }
    }

    // Calculates the brightness of an image matrix.
    // This method calculates the brightness of an image represented by a boolean matrix
    // where 'true' indicates a white pixel and 'false' indicates a black pixel.
    private double getBrightness(boolean[][] imgMatrix) {
        int numWhitePixels = 0;
        int numPixels = imgMatrix.length * imgMatrix[0].length;
        for (boolean[] row : imgMatrix) {
            for (boolean pixel : row) {
                if (pixel) {
                    numWhitePixels++;
                }
            }
        }
        return (double) numWhitePixels / numPixels;
    }

    /**
     * Gets the character corresponding to the given image brightness.
     * @param brightness The brightness value of the image.
     * @return The character matching the given brightness.
     * @throws EmptyCharSetException if the character set is empty.
     */
    public char getCharByImageBrightness(double brightness) throws EmptyCharSetException {
        if (charBrightnessMap.isEmpty()) {
            throw new EmptyCharSetException();
        }
        // Normalize the brightness to be between 0 and 1.
        brightness = brightness * (maxBrightness - minBrightness) + minBrightness;

        Double closestKey;
        Double ceilingKey = charBrightnessMap.ceilingKey(brightness);
        Double floorKey = charBrightnessMap.floorKey(brightness);
        if (ceilingKey == null) {
            closestKey = floorKey;
        } else if (floorKey == null) {
            closestKey = ceilingKey;
        } else {
            closestKey = Math.abs(ceilingKey - brightness) < Math.abs(floorKey - brightness) ?
                    ceilingKey : floorKey;
        }
        return charBrightnessMap.get(closestKey).first();
    }

    /**
     * Adds a character to the character set along with its brightness value.
     * @param c The character to add.
     */
    public void addChar(char c) {
        Double brightness = getBrightness(CharConverter.convertToBoolArray(c));

        if (charBrightnessMap.containsKey(brightness)) {
            charBrightnessMap.get(brightness).add(c);
        } else {
            TreeSet<Character> charList = new TreeSet<>();
            charList.add(c);
            charBrightnessMap.put(brightness, charList);
        }
        if (maxBrightness == null || brightness > maxBrightness) {
            maxBrightness = brightness;
        }
        if (minBrightness == null || brightness < minBrightness) {
            minBrightness = brightness;
        }

    }

    /**
     * Removes a character from the character set.
     * @param c The character to remove.
     */
    public void removeChar(char c) {
        for (Double brightness : charBrightnessMap.keySet()) {
            for (Character character : charBrightnessMap.get(brightness)) {
                if (character.equals(c)) {
                    charBrightnessMap.get(brightness).remove(c);
                    if (charBrightnessMap.get(brightness).isEmpty()) {
                        charBrightnessMap.remove(brightness);
                        if (charBrightnessMap.isEmpty()) {
                            maxBrightness = null;
                            minBrightness = null;
                            return;
                        }
                        if (brightness.equals(maxBrightness)) {
                            maxBrightness = charBrightnessMap.lowerKey(brightness);
                        }
                        if (brightness.equals(minBrightness)) {
                            minBrightness = charBrightnessMap.higherKey(brightness);
                        }
                        return;
                    }
                    break;
                }
            }
        }
    }
}

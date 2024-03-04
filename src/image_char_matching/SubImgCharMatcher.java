package image_char_matching;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;


public class SubImgCharMatcher {

    private  TreeMap<Double, TreeSet<Character>> charBrightnessMap = new TreeMap<>();
    /**
     * A flag to indicate if the brightness of the characters in the map needs to be normalized.
     * After normalize once this flag set to be false until we added a new char with grater brightness then
     * the max brightness or a new char with less brightness then the min brightness.
     */
    private boolean isNeedToNormalize = true;
    private Double maxBrightness;
    private Double minBrightness;

    public SubImgCharMatcher(char[] charset) {
        maxBrightness = null;
        minBrightness = null;
        for (char c : charset) {
            addChar(c);
        }
    }

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
    public char getCharByImageBrightness(double brightness) {
        //todo if map is empty
        Double closestKey;
        Double ceilingKey = charBrightnessMap.ceilingKey(brightness);
        Double floorKey = charBrightnessMap.floorKey(brightness);
        if (ceilingKey == null) {
            closestKey = floorKey;
        }
        else if (floorKey == null) {
            closestKey = ceilingKey;
        }
        else {
            closestKey = Math.abs(ceilingKey - brightness) < Math.abs(floorKey - brightness) ?
                    ceilingKey : floorKey;
        }
        return charBrightnessMap.get(closestKey).first();
    }

    public void addChar(char c) {
        Double brightness = getBrightness(CharConverter.convertToBoolArray(c));
        if (charBrightnessMap.containsKey(brightness)) {
            charBrightnessMap.get(brightness).add(c);// add the character to the set of
            // characters with the same brightness in sorted order by ascii value
        } else {
            TreeSet<Character> charList = new TreeSet<>();
            charList.add(c);
            charBrightnessMap.put(brightness, charList);
        }
        if (maxBrightness == null || brightness > maxBrightness) {
            maxBrightness = brightness;
            isNeedToNormalize = true;
        }
        if (minBrightness == null || brightness < minBrightness) {
            minBrightness = brightness;
            isNeedToNormalize = true;
        }
    }

    public void removeChar(char c) {
        //we prefer to go through the map and not to use getBrightness method because getBrightness does
        // 16*16 and the max size of the map is 132 (for all ascii characters).
        for (Double brightness : charBrightnessMap.keySet()) {
            for (Character character : charBrightnessMap.get(brightness)) {
                if (character.equals(c)) {
                    charBrightnessMap.get(brightness).remove(c);
                    if (charBrightnessMap.get(brightness).isEmpty()) {
                        charBrightnessMap.remove(brightness);
                        if (brightness.equals(maxBrightness) || brightness.equals(minBrightness)) {
                            isNeedToNormalize = true; //no need to update maxBrightness or minBrightness
                            // because the normalizing method will do it.
                        }
                        return;
                    }
                    break;
                }
            }
        }
    }


    /**
     * Normalizes the brightness of the characters in the map to be between 0 and 1.
     * use this method just before displaying the image.
     */
    public void normalizeBrightness() {
        if (!isNeedToNormalize) {
            return;
        }
        TreeMap <Double, TreeSet<Character>> newCharBrightnessMap = new TreeMap<>();
        Double minBrightness = charBrightnessMap.firstKey();
        Double maxBrightness = charBrightnessMap.lastKey();
        for (Double brightness : charBrightnessMap.keySet()) {
            Double newBrightness = (brightness - minBrightness) / (maxBrightness - minBrightness);
            newCharBrightnessMap.put(newBrightness, charBrightnessMap.get(brightness));
        }
        charBrightnessMap = newCharBrightnessMap;
        this.minBrightness = charBrightnessMap.firstKey();
        this.maxBrightness = charBrightnessMap.lastKey();
        isNeedToNormalize = false;
    }


}

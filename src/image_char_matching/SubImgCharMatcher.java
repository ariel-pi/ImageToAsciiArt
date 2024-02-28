package image_char_matching;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;


public class SubImgCharMatcher {

    private  TreeMap<Double, TreeSet<Character>> charBrightnessMap = new TreeMap<>();

    public SubImgCharMatcher(char[] charset) {
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
    }

    public void removeChar(char c) {
        charBrightnessMap.remove(c);
    }


    /**
     * Normalizes the brightness of the characters in the map to be between 0 and 1.
     * use this method just before displaying the image.
     */
    private void normalizeBrightness() {
        TreeMap <Double, TreeSet<Character>> newCharBrightnessMap = new TreeMap<>();
        Double minBrightness = charBrightnessMap.firstKey();
        Double maxBrightness = charBrightnessMap.lastKey();
        for (Double brightness : charBrightnessMap.keySet()) {
            Double newBrightness = (brightness - minBrightness) / (maxBrightness - minBrightness);
            newCharBrightnessMap.put(newBrightness, charBrightnessMap.get(brightness));
        }
        charBrightnessMap = newCharBrightnessMap;
    }


}

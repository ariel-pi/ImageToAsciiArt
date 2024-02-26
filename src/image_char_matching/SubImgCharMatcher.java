package image_char_matching;

import java.util.HashMap;

public class SubImgCharMatcher {

    private final HashMap<Character, Double> charBrightnessMap = new HashMap<>();

    public SubImgCharMatcher(char[] charset) {
        for (char c : charset) {
            charBrightnessMap.put(c, 0.0);
        }

    }

    public char getCharByImageBrightness(double brightness) {
        return 't'; //todo: implement
    }

    public void addChar(char c) {
        charBrightnessMap.put(c, 0.0);
    }

    public void removeChar(char c) {
        charBrightnessMap.remove(c);
    }


}

package image;

/**
 * Represents a sub-image extracted from a larger image.
 * Each sub-image contains an instance of the Image class and may have an associated brightness value.
 * @see Image
 *
 * Author: Ariel Pinhas, Amiel Wreschner
 */
public class SubImage {

    /**
     * Constant representing null brightness value.
     */
    public static final int NULL_BRIGHTNESS = -1;

    private final Image image;
    private double brightness;

    /**
     * Constructs a SubImage object with the given Image.
     * The initial brightness value is set to NULL_BRIGHTNESS.
     * @param image The Image object representing the sub-image.
     */
    public SubImage(Image image) {
        this.image = image;
        this.brightness = NULL_BRIGHTNESS;
    }

    /**
     * Gets the Image object associated with this sub-image.
     * @return The Image object representing the sub-image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the brightness value of this sub-image.
     * @return The brightness value.
     */
    public double getBrightness() {
        return brightness;
    }

    /**
     * Sets the brightness value of this sub-image.
     * @param brightness The brightness value to set.
     */
    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
}

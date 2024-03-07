package image;

public class SubImage{
    public static final int NULL_BRIGHTNESS = -1;
    private final Image image;
    private double brightness;
    public SubImage(Image image) {
        this.image = image;
        this.brightness = NULL_BRIGHTNESS;
    }

    public Image getImage() {
        return image;
    }
    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
}

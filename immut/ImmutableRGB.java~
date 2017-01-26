package immut;

// If it's immutable, it's thread safe.
final public class ImmutableRGB {        // disallow subclassing
    final private int red;
    final private int green;
    final private int blue;
    final private String name;

    private void check(int red,
                       int green,
                       int blue) {
        // in range?
        if (red < 0   || red > 255 || 
            green < 0 || green > 255 || 
            blue < 0  || blue > 255) {
            throw new IllegalArgumentException();
        }
    }
    
    // create an ImmutableRGB instance
    public ImmutableRGB(int red,
                        int green,
                        int blue,
                        String name) {
        check(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
    }
 
    // for convenience and efficiency, store the RGB values in a single int
    public int getRGB() {
        return ((red << 16) | (green << 8) | blue);
    }

    public String getName() {
        return name;
    }

    // create a _new_ ImmutableRGB object
    public ImmutableRGB invert() {
        return new ImmutableRGB(255 - red,
                       255 - green,
                       255 - blue,
                       "Inverse of " + name);
    }
}

package immut;

/**
   Immutable data structures are thereby thread-safe, an approach popularized in
   Clojure, a modern Lisp that runs on the JVM.

   The class ImmutableRGB (Red-Green-Blue) is an illustration of a 
   programmer-defined immutable type. Once created, an ImmutableRGB instance
   cannot be changed. In more technical language, the state of an 
   ImmutableRGB instance can't be altered throughout its lifetime.
 */
final public class ImmutableRGB { // disallow subclassing
    final private int red;
    final private int green;
    final private int blue;
    final private String name;

    // Check the ranges: must be from 0 through 255.
    private void check(int red,
                       int green,
                       int blue) {
        if (red < 0   || red > 255 || 
            green < 0 || green > 255 || 
            blue < 0  || blue > 255) {
            throw new IllegalArgumentException();
        }
    }
    
    // Create an ImmutableRGB instance
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
 
    // For convenience and efficiency, store the RGB values in a single 32-bit int.
    public int getRGB() {
        return ((red << 16) | (green << 8) | blue);
    }

    public String getName() {
        return name;
    }

    // Create a _new_ ImmutableRGB object -- no change to the original.
    public ImmutableRGB invert() {
        return new ImmutableRGB(255 - red,
                       255 - green,
                       255 - blue,
                       "Inverse of " + name);
    }
}

package immut;

/**
   Immutable data structures (e.g., java.lang.String) are thereby thread-safe, an approach popularized in
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
}

/** 
 * Inverting a single instance of an ImmutableRGB is not expensive, but imagine inverting a huge list 
 * of such instances. Once collections (sets, lists, maps) are in play, the issue of efficiency
 * jumps to the forefront for immutable types. The java.util.concurrent package
 * has various collection types that ensure or at least promote thread-safety. This extended 
 * documentation highlights two of these to explore modern approaches to making collections
 * thread-safe yet reasonably efficient.
 *
 * The CopyOnWriteArrayList adapts an approach from memory-management in modern OSes. When one process spawns
 * another in a modern OS, the former is the 'parent' and the latter the 'child'. The child inherits the 'pages'
 * (memory segments) in the parent's address space. However, if either the parent or the child performs a
 * 'write' operation on a given page, then a copy must be made: the page is no longer shared between the two.
 * This approach is known by the acronym COW.
 *
 * As the name indicates, that's the approach taken in the CopyOnWriteArrayList, a thread-safe version of the 
 * java.util.ArrayList. Consider this scenario. Thread t1 is traversing such a list, performing only 'read' operations. 
 * At the same time, thread t2 'writes' to the list, thereby mutating it. Both operations can proceed without 
 * incident because the traversal occurs on the list as it was the instant the traversal began; and the 
 * 'write' operation generates a _copy_ the original list. In the traversal, t1 might not pick up the change 
 * that t2 makes, depending on whether the 'write' operation precedes the start of the traversal.
 * In applications where 'read' traversals are more common than 'write' operations, this is highly efficient.  
 * The COW approach means, in short, that 'read' ('non-mutating') operations need not be locked.
 * 
 * The ConcurrentHashMap has the same specification as the age-old java.util.Hashtable, which is thread-safe; but the
 * former is more efficient than the latter. Once again, the implementation rests on distinguishing between
 * 'read' (non-mutating) and 'write' (mutating) operations. There is no need to lock an entire ConcurrentHashMap instance,
 * and rarely a need to lock any particular 'read' ('retrieval') operation, which works on a snapshot of the map that 
 * reflects the most recent _completed_ update. If thread t1 is reading a particular value at the same time that thread t2 is
 * updating that value, then t1 reads the pre-updated value in a spapshot of the original key/value pair. 
 */

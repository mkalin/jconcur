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

/** 
 * The Clojure approach of making data structures immutable by default can lead to inefficiency when 
 * collections such as sets, lists, and maps are in play. For example, if adding to a huge list results
 * in a new copy of this list, there's a penalty in time and memory for the copy. The data structures in 
 * java.util.concurrent take various approaches in trying to promote thread-safety but without 
 * unduly compromising efficiency. Two examples illustrate. For more detail, see the JavaDoc.
 *
 * The CopyOnWriteArrayList adapts an approach from memory-management in modern OSes. When one process spawns
 * another in a modern OS, the former is the 'parent' and the latter the 'child'. The child inherits the 'pages'
 * (memory segments) in the parent's address space. However, if either the parent or the child performs a
 * 'write' operation on a given page, then a copy must be made: the page is no longer shared between the two.
 *
 * As the name indicates, that's the approach taken in the CopyOnWriteArrayList, a thread-safe version of the 
 * java.util.ArrayList. Consider this scenario. Thread t1 is traversing such a list, performing only 'read' operations. 
 * At the same time, thread t2 'writes' to the list, thereby mutating it. Both operations can proceed without 
 * incident because the traversal occurs on a snapshot of the list as it was when the traversal began; and the 
 * 'write' operation occurs on a _copy_ of the original list. In the traversal, t1 would not pick up the change 
 * that t2 makes. In applications where 'read' traversals are more common than 'write' operations, this is highly efficient.  
 * The COW approach means, in short, that 'read' operations need not be locked.
 * 
 * The ConcurrentHashMap has the same specification as the age-old java.util.Hashtable, which is thread-safe; but the
 * former is more efficient than the latter. Once again, the implementation rests on distinguishing between
 * 'read' (non-mutating) and 'write' (mutating) operations. There is no need to lock an entire ConcurrentHashMap instance,
 * and rarely a need to lock any particular 'read' ('retrieval') operation, which works on a snapshot of the map that 
 * reflects the most recent _completed_ update. If thread t1 is reading a particular value at the same time that thread t2 is
 * updating that value, then t1 reads the pre-updated value in a spapshot of the original key/value pair. 
 */

package review;

import java.util.concurrent.atomic.AtomicInteger;

/**
   Two classes to illustrate explicit versus 'baked in' synchronization.
   For convenience, both classes are in the same file, but the 
   SynchronizedCounter class otherwise would be public and in its own file.
 */

final public class AtomicCounter {
    private AtomicInteger c = new AtomicInteger(0);  // baked-in synchronization

    public void increment() {
        c.incrementAndGet();  // increment counter
    }
    
    public void decrement() { 
        c.decrementAndGet(); // decrement counter
    }
    
    public int value() {
        return c.get();      // fetch counter's current value
    }
}

/** Here's the old way of doing the same. If a thread executes any one of these
    three methods, then mutual exclusion is ensured: no other thread can execute 
    any of the three methods until the lock is released.

    The implicit lock is 'this', a reference to a specific SynchronizedCounter instance.

    Instead of synchronized methods, the newer ReentrantLock might be used, as an earlier
    example illustrated.
*/
final class SynchronizedCounter { // would be 'public class' and in its own file
    private int c = 0;
    
    public synchronized void increment() {
        c++;
    }

    public synchronized void decrement() {
        c--;
    }

    public synchronized int value() {
        return c;
    }
}

/** Self-test exercise:
 *
 * Write a program to confirm that the SynchronizedCounter class does ensure mutual exclusion
 * among the three 'synchronized' methods increment, decrement, and value.
 */


import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    private AtomicInteger c = new AtomicInteger(0);

    public void increment() {
        c.incrementAndGet();
    }
    
    public void decrement() {
        c.decrementAndGet();
    }
    
    public int value() {
        return c.get();
    }
}

/** Here's the old way of doing the same. If a thread executes any one of these
    three methods, then mutual exclusive is ensured: no other thread can execute 
    either of the other two methods. 

    The implicit lock is 'this', a reference to a specific SynchronizedCounter instance.
   
*/
class SynchronizedCounter { // would be 'public class' and in its own file
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

/** Self-test question:

    What would be the impact on multithreading if the field declaration 

    private int c = 0;

    were changed to

    private static int c = 0;  // make the counter implementation a static field.

    (Assume the SynchronizedCounter class is public.)
 */

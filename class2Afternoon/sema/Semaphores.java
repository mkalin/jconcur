package sema;

/**
 * A mutex is a a mutual-exclusion construct: wrapping statements within a mutex ensures 
 * that, at most, one thread can execute the wrapped statements. In technical terms, a 
 * mutex is a binary semaphore, a thread-synchronization construct with exactly
 * two values, 0 (access permitted) or 1 (no access permitted). 
 *
 * However, semaphores can be generalized beyond mutexes, in which case
 * they're known as counting semaphores: the values go beyond 1 to some
 * specified limit. In high-level terms, a counting semphore can be described 
 * as a set of permits (e.g., a set of four). The logic is then:
 *
 * -- If there's a permit available, give it to a thread that invokes acquire().
 * -- If not, block the thread.
 * -- If a thread holding a permit calls release(), return the permit to the available set.
 * 
 * This code example, adapted from the JavaDoc, illustrates. A set of threads
 * contests for resources (Resource1, Resource2,...), and the application
 * uses a counting semahore to regulate how many resources can be checked out at
 * a time. A sample run generates output such as:
 *  ...
 *  pool-7-thread-1 putting back Resource2
 *  pool-2-thread-1 acquiring Resource2
 *  pool-11-thread-1 putting back Resource1
 *  pool-7-thread-1 acquiring Resource1
 *  pool-1-thread-1 putting back Resource6
 *  pool-11-thread-1 acquiring Resource6
 *  pool-5-thread-1 putting back Resource0
 *  pool-1-thread-1 acquiring Resource0
 *  pool-6-thread-1 putting back Resource7
 *  pool-5-thread-1 acquiring Resource7
 *  pool-8-thread-1 putting back Resource8
 *  pool-6-thread-1 acquiring Resource8
 *  pool-10-thread-1 putting back Resource3
 *  ...
 *
 * The program runs indefinitely: kill at the command-line with Control-C.
 */

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class Semaphores {
    public static void main(String[] args) {
	new Semaphores().demo();
    }

    private void demo() {
	final Pool pool = new Pool(); // pool of Resources to be acquired

	// The run() method continually vies for a resource by requesting a 
	// permit; keeps the resource a random number of ticks; and then returns 
	// the resource to the pool.
	Runnable r = new Runnable() {
		@Override
		public void run() {
		    String name = Thread.currentThread().getName();
		    try {
			while (true) {
			    String resource;
			    System.out.printf("%s acquiring %s%n", name,
					      resource = pool.getResource());

			    Thread.sleep(200 + (int) (Math.random() * 100)); // simulates using a Resource
			    System.out.printf("%s putting back %s%n",
					      name,
					      resource);
			    pool.putResource(resource);
			}
		    }
		    catch (InterruptedException ie) {
			System.out.printf("%s interrupted\n", name);
		    }
		}
	    };
	
	// Create an ExecutorService to execute multiple instances of the task-at-hand,
	// which is get a resource, hold it awhile, and then return it to the pool.
	ExecutorService[ ] executors = new ExecutorService[Pool.MaxPermits + 1];
	for (int i = 0; i < executors.length; i++) {
	    executors[i] = Executors.newSingleThreadExecutor();
	    executors[i].execute(r);
	}
    }
}

/**
 * The Pool comprises resource, in this case an array of strings for which threads contend.
 * To get a resource, a thread needs a permit, an instance of a counting semaphore with a 
 * value of MaxPermits (currently 10). 
 *
 * Two methods are synchronized on the pool instance:
 *
 * -- getNextAvailableResource(), which searches for an available resource and returns
 *    it, or null in case there is no such resource.
 *
 * -- isResourceFree(), which checks whether a given resource is available.
 *
 * The Semaphore methods acquire() and release() are themselves thread-safe.
 */
final class Pool {
    public static final int MaxPermits = 10; 
    private Semaphore permit = new Semaphore(MaxPermits, true); // true == guarantee FIFO behavior
    private String[ ] resources;
    private boolean[ ] used = new boolean[MaxPermits];
    
    Pool() {
	resources = new String[MaxPermits];
	for (int i = 0; i < resources.length; i++) resources[i] = "Resource" + i;
    }
    
    String getResource() throws InterruptedException {
	permit.acquire(); // blocks until a permit is available (or an exception thrown)
	return getNextAvailableResource(); // with permit in hand, get the resource
    }
    
    void putResource(String resource) {
	if (isResourceFree(resource)) permit.release(); // release the permit into the pool
    }
    
    // This method and the next are synchronized on the Pool
    // instance because both access the used array: this
    // method searches for a free resource and, if successful,
    // marks the resource as used before returning it. The
    // method below searches for a specified, currently-in-use resource and, 
    // if successful, marks the resource as free.
    private synchronized String getNextAvailableResource() {
	for (int i = 0; i < MaxPermits; ++i) {
	    if (!used[i]) {
		used[i] = true;
		return resources[i];
	    }
	}
	return null; // nothing available
    }
    
    // See documentation immediately above.
    private synchronized boolean isResourceFree(String resource) {
	for (int i = 0; i < MaxPermits; ++i) {
	    if (resource == resources[i]) {
		if (used[i]) {
		    used[i] = false;
		    return true;
		}
		else
		    return false;
	    }
	}
	return false;
    }
}

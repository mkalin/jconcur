/**
 * A mutex is a a mutual-exclusion construct: wrapping statements within a mutex ensures 
 * that, at most, one thread can execute the wrapped statements. In technical terms, a 
 * mutex is a binary semaphore, a thread-synchronization construct with exactly
 * two values, 0 (access permitted) or 1 (no access permitted). 
 *
 * However, semaphores can be generalized beyond mutexes, in which case
 * they're known as counting semaphores: the value go go beyond 1 to some
 * specified limit. In high-level terms, a counting semphore can be described 
 * as a set of permits (e.g., a set of four). The logic is then:
 *
 * If there's a permit available, give it to a thread that invokes acquire().
 * If not, block the thread.
 * If a thread holding a permit calls release(), return the permit to the set.
 * 
 * This code example, adapted from the javadoc, illustrates. A set of threads
 * contents for string resources (Resource1, Resource2,...), and the application
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
 */

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class Semaphores {
    public static void main(String[] args) {
	final Pool pool = new Pool();
	Runnable r = new Runnable() {
		@Override
		public void run() {
		    String name = Thread.currentThread().getName();
		    try {
			while (true) {
			    String resource;
			    System.out.printf("%s acquiring %s%n", name,
					      resource = pool.getResource());
			    Thread.sleep(200 + (int) (Math.random() * 100));
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
	ExecutorService[ ] executors = new ExecutorService[Pool.MaxAvailable + 1];
	for (int i = 0; i < executors.length; i++) {
	    executors[i] = Executors.newSingleThreadExecutor();
	    executors[i].execute(r);
	}
    }
}

final class Pool {
    public static final int MaxAvailable = 10;
    private Semaphore available = new Semaphore(MaxAvailable, true); // true == guarantee FIFO behavior
    private String[ ] resources;
    private boolean[ ] used = new boolean[MaxAvailable];
    
    Pool() {
	resources = new String[MaxAvailable];
	for (int i = 0; i < resources.length; i++) resources[i] = "Resource" + i;
    }
    
    String getResource() throws InterruptedException {
	available.acquire();
	return getNextAvailableResource();
    }
    
    void putResource(String resource) {
	if (markAsUnused(resource)) available.release();
    }
    
    // This method and the next are synchronized on the Pool
    // instance because both access the used array: this
    // method searches for an unused resource and, if successful,
    // marks the resource as available before returning it. The
    // method below searches for a specified, currently-in-use resource and, 
    // if successful, marks the resource as unused.
    private synchronized String getNextAvailableResource() {
	for (int i = 0; i < MaxAvailable; ++i) {
	    if (!used[i]) {
		used[i] = true;
		return resources[i];
	    }
	}
	return null; 
    }
    
    private synchronized boolean markAsUnused(String resource) {
	for (int i = 0; i < MaxAvailable; ++i) {
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

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
 * This code example illustrates how counting semaphores can be used. A set of threads
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
	final ResourcePool pool = new ResourcePool(); // pool of Resources to be acquired

	// The run() method continually vies for a resource by requesting a 
	// permit; keeps the resource a random number of ticks; and then returns 
	// the resource to the pool.
	Runnable resourceUser = new Runnable() {
		@Override
		public void run() {
		    String name = Thread.currentThread().getName();
		    try {
			while (true) {
			    Resource resource = pool.getResource();

			    if (resource != null) {
				System.out.printf("%s acquiring %s%n", name, resource.getName());
				Thread.sleep(200 + (int) (Math.random() * 100)); // simulates using a Resource
				System.out.printf("%s putting back %s%n", name, resource.getName());

				pool.putResource(resource);
			    }
			}
		    }
		    catch (InterruptedException ie) {
			System.out.printf("%s interrupted\n", name);
		    }
		}
	    };
	
	// Create an ExecutorService to execute a Runnable resourceUser,
	// who tries get a resource, then holds it awhile, and finally returns it to the pool.
	// To promote contention, there's one more thread contending for resources than
	// there are resource permits.
	ExecutorService[ ] executors = new ExecutorService[ResourcePool.MaxPermits + 1];
	for (int i = 0; i < executors.length; i++) {
	    executors[i] = Executors.newSingleThreadExecutor();
	    executors[i].execute(resourceUser);
	}
    }
}

class Resource {
    String name;
    boolean available;

    Resource(String name) {
	this.name = name;
	this.available = true;
    }

    String getName() { return name; }
    boolean isAvailable() { return available; }
    void setAvailable(boolean available) { this.available = available; }
}

/**
 * The ResourcePool comprises resources, in this case an array of strings for which threads contend.
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
final class ResourcePool {
    public static final int MaxPermits = 10; 
    private Semaphore permit;
    private Resource[ ] resources;
    
    ResourcePool() {
	permit = new Semaphore(MaxPermits, true); // true == guarantee FIFO behavior
	resources = new Resource[MaxPermits]; 

	for (int i = 0; i < resources.length; i++) 
	    resources[i] = new Resource("Resource" + i);
    }
    
    Resource getResource() throws InterruptedException {
	permit.acquire(); // blocks until a permit is available (or an exception is thrown)
	return getAvailableResource(); // with permit in hand, get the resource
    }
    
    // Return a Resource to the ResourcePool.
    void putResource(Resource resource) {
	// Once the Resource is returned to the ResourcePool, release the permit.
	if (resourceHasBeenReturned(resource))
	    permit.release(); // release the permit into the pool
    }
    
    // Get an available Resource, if any. (Synchronized with method
    // resourceHasBeenReturned to avoid a data race.)
    private synchronized Resource getAvailableResource() {
	for (int i = 0; i < resources.length; ++i) {
	    if (resources[i].isAvailable()) {
		resources[i].setAvailable(false); 
		return resources[i];
	    }
	}
	return null; // nothing available
    }
    
    // Confirm that a Resource has been returned to the pool. (Synchronized with
    // method getAvailableResource to avoid a data race.)
    private synchronized boolean resourceHasBeenReturned(Resource resource) {
	// If the resource isn't out to a user, and so is already available, 
	// it cannot be returned.
	if (resource.isAvailable())
	    return false;
	
	// Otherwise, mark the resource as now available, which effectively returns
	// it to the ResourcePool for further use.
	resource.setAvailable(true);
	return true;
    }
}

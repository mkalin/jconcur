/* Overview
   
   A mutex, as the name suggests, is a a mutual-exclusion construct:
   wrapping statements within a mutex ensures that, at most, one thread
   can execute the wrapped statements. In technical terms, a mutex is
   a binary semaphore, a thread-synchronization construct with exactly
   two values, 0 (access permitted) or 1 (no access permitted). 

   However, semaphores can be generalized beyond mutexes, in which case
   they're known as counting semaphores: in technical terms, the value
   can go beyond 1 to some specified limit. In high-level terms, a 
   counting semphore can be described as a set of permits (e.g., a set
   of four). The logic is then:

   If there's a permit available, give it to a thread that invokes acquire().
   If not, block the thread.
   If a thread holding a permit calls release(), return the permit to the set.
   
   This code example, adapted from the javadoc, illustrates. A set of threads
   contents for string resources (Item1, Item2,...), and the application
   uses a counting semahore to regulate how many items can be checked out at
   a time. A sample run generates output such as, with the threads housed
   in Executor pools:

   ...
   pool-7-thread-1 putting back Item2
   pool-2-thread-1 acquiring Item2
   pool-11-thread-1 putting back Item1
   pool-7-thread-1 acquiring Item1
   pool-1-thread-1 putting back Item6
   pool-11-thread-1 acquiring Item6
   pool-5-thread-1 putting back Item0
   pool-1-thread-1 acquiring Item0
   pool-6-thread-1 putting back Item7
   pool-5-thread-1 acquiring Item7
   pool-8-thread-1 putting back Item8
   pool-6-thread-1 acquiring Item8
   pool-10-thread-1 putting back Item3
   ...
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
			    String item;
			    System.out.printf("%s acquiring %s%n", name,
					      item = pool.getItem());
			    Thread.sleep(200 + (int) (Math.random()*100));
			    System.out.printf("%s putting back %s%n",
					      name,
					      item);
			    pool.putItem(item);
			}
		    }
		    catch (InterruptedException ie) {
			System.out.printf("%s interrupted%n", name);
		    }
		}
	    };
	ExecutorService[] executors = new ExecutorService[Pool.MAX_AVAILABLE + 1];
	for (int i = 0; i < executors.length; i++) {
	    executors[i] = Executors.newSingleThreadExecutor();
	    executors[i].execute(r);
	}
    }
}

final class Pool {
    public static final int MAX_AVAILABLE = 10;
    private Semaphore available = new Semaphore(MAX_AVAILABLE, true);
    private String[] items;
    private boolean[] used = new boolean[MAX_AVAILABLE];
    
    Pool() {
	items = new String[MAX_AVAILABLE];
	for (int i = 0; i < items.length; i++) items[i] = "Item" + i;
    }
    
    String getItem() throws InterruptedException {
	available.acquire();
	return getNextAvailableItem();
    }
    
    void putItem(String item) {
	if (markAsUnused(item)) available.release();
    }
    
    private synchronized String getNextAvailableItem() {
	for (int i = 0; i < MAX_AVAILABLE; ++i) {
	    if (!used[i]) {
		used[i] = true;
		return items[i];
	    }
	}
	return null; // not reached
    }
    
    private synchronized boolean markAsUnused(String item) {
	for (int i = 0; i < MAX_AVAILABLE; ++i) {
	    if (item == items[i]) {
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
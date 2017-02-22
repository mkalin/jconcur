package basicC;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A 2nd approach: use the ExecutorService in which 100 BasicCounter instances
 * are multiplexed onto 10 threads in a fixed-sized thread pool.
 * In effect, the BasicCounter instances are 100 jobs-to-be-done, and the
 * the under-the-hood pooled threads are the workers doing the jobs.
 */
public class RunBasicCounter2 {
    private static final int poolSize = 10;

    public static void main(String[ ] args) {
	final int howMany = 100;

	ExecutorService executor = Executors.newFixedThreadPool(poolSize);

	for (int i = 0; i < howMany; i++) {
	    Runnable counter = new BasicCounter(10_000_000L + i);
	    executor.execute(counter); // Executor now manages the counter
	}
	executor.shutdown(); // accept no new threads

	while (!executor.isTerminated()) {
	    // await thread deaths (for now, we'll "busy wait" by looping)
	}
	System.out.println("All started threads have died.");
    }
}

/** Output from a sample run:

    pool-1-thread-6: 50000045000010
    pool-1-thread-1: 49999995000000
    pool-1-thread-5: 50000035000006
    pool-1-thread-3: 50000015000001
    pool-1-thread-2: 50000005000000
    pool-1-thread-7: 50000055000015
    pool-1-thread-4: 50000025000003
    pool-1-thread-9: 50000075000028
    ...
    pool-1-thread-6: 50000925004278
    pool-1-thread-5: 50000835003486
    pool-1-thread-7: 50000965004656
    pool-1-thread-2: 50000985004851
    pool-1-thread-3: 50000955004560
    pool-1-thread-4: 50000975004753
    All started threads have died.
*/

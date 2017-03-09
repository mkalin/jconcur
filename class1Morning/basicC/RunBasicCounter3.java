package basicC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 3rd approach: use an ExecutorService to manage 20,000 Future deliverables,
 * where a Future result is the work done by a CallableBasicCounter instance.
 */
public class RunBasicCounter3 {
    private static final int poolSize = 10;

    public static void main(String[ ] args) {
	final int howMany = 20_000; 

	ExecutorService executor = Executors.newFixedThreadPool(poolSize);
	List<Future<Long>> list = new ArrayList<Future<Long>>();

	for (int i = 0; i < howMany; i++) {
	    Callable<Long> counter = new CallableBasicCounter(10_000_000L + i);
	    Future<Long> submit = executor.submit(counter);
	    list.add(submit);
	}
	executor.shutdown(); // no more submitted work

	long sum = 0;
	System.out.println("List size: " + list.size());

	// Await the result and retrieve it.
	for (Future<Long> future : list) {
	    try {
		// get() blocks until a computation is finished
		// There's also:  get(long timeout, TimeUnit unit
		sum += future.get(); 
	    } 
	    catch (InterruptedException e) {
		e.printStackTrace();
	    } 
	    catch (ExecutionException e) {
		e.printStackTrace();
	    }
	}
	System.out.println("Sum: " + sum);
    }
}

/** Output from sample run:

    pool-1-thread-4:  50000025000003
    pool-1-thread-1:  49999995000000
    pool-1-thread-6:  50000045000010
    pool-1-thread-3:  50000015000001
    pool-1-thread-5:  50000035000006
    pool-1-thread-2:  50000005000000
    pool-1-thread-1:  50000105000055
    pool-1-thread-6:  50000115000066
    pool-1-thread-4:  50000095000045
    List size: 20000
    pool-1-thread-10: 50000085000036
    ...
    pool-1-thread-10: 50200124850028
    pool-1-thread-2:  50200134870021
    pool-1-thread-9:  50200154910010
    pool-1-thread-4:  50200084770066
    pool-1-thread-3:  50200164930006
    pool-1-thread-7:  50200144890015
    pool-1-thread-5:  50200174950003
    pool-1-thread-6:  50200184970001
    Sum: 1002001133133340000
*/

package basicC;

import java.util.ArrayList;
import java.util.List;

/**
 * A low-level approach to managing multiple threads:
 *
 *  -- create 100 BasicCounter instances, start each
 *
 *  -- run until all of the threads terminate
 */
public class RunBasicCounter1 {
    public static void main(String[] args) {
	List<Thread> threads = new ArrayList<Thread>();
	final int howMany = 100;

	int k = 1;
	for (int i = 0; i < howMany; i++) {
	    // Constructor arg is how many times to have the counter count.
	    Runnable task = new BasicCounter(10_000_000L + i);  // a Runnable doesn't have a start() method
	    Thread counter = new Thread(task);                  // a Thread does have a start() method

	    counter.setName("T" + String.valueOf(k++));

	    //## A constructed thread _must_ be started to enable multithreading.
	    //## Note that start(), _not_ run(), is invoked.
	    counter.start(); //## the thread will start to run sometime hereafter; start() returns at once

	    threads.add(counter); // Keep track of started threads.
	}

	// One way to have the main-thread wait for all of the explicitly started
	// threads to terminate. There are various other ways to do the same.
	int alive = 0;
	do {
	    alive = 0;
	    for (Thread thread : threads) 
		if (thread.isAlive()) alive++;
	} while (alive > 0);

	// At this point, the main-thread (the thread that executes main and
	// whatever else is called from main) is still alive, but is about to exit main
	// and thereby terminate.
	System.out.println("main-thread is about to exit...");
    }
}

/** Output (slightly formatted) from a sample run:

    T1:  49999995000000
    T2:  50000005000000
    T6:  50000045000010
    T5:  50000035000006
    T4:  50000025000003
    T3:  50000015000001
    T8:  50000065000021
    T7:  50000055000015
    T9:  50000075000028
    T10: 50000085000036
    T14: 50000125000078
    T12: 50000105000055
    T11: 50000095000045
    T15: 50000135000091
    T13: 50000115000066
    T21: 50000195000190
    T19: 50000175000153
    T17: 50000155000120
    T16: 50000145000105
    T22: 50000205000210
    T18: 50000165000136
    T20: 50000185000171
    T25: 50000235000276
    T24: 50000225000253
    ...
*/

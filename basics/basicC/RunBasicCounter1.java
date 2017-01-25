package basicC;

import java.util.ArrayList;
import java.util.List;

// A low-level approach to managing multiple threads:
//  -- create 100 BasicCounter instances, start each
//  -- run until all of the threads terminate
public class RunBasicCounter1 {
    public static void main(String[] args) {
	List<Thread> threads = new ArrayList<Thread>();
	final int howMany = 100;

	int k = 1;
	for (int i = 0; i < howMany; i++) {
	    Runnable task = new BasicCounter(10000000L + i);
	    Thread counter = new Thread(task);
	    counter.setName(String.valueOf(k++));
	    counter.start();      // start() returns immediately
	    threads.add(counter);
	}

	int alive;
	do {
	    alive = 0;
	    for (Thread thread : threads) 
		if (thread.isAlive()) alive++;
	} while (alive > 0);
    }
}
/* output
50000055000015
50000005000000
50000035000006
50000065000021
50000025000003
50000015000001
...
*/

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

/** Output from a sample run:

      1: 49999995000000
      2: 50000005000000
      5: 50000035000006
      7: 50000055000015
      4: 50000025000003
      3: 50000015000001
      6: 50000045000010
      8: 50000065000021
      9: 50000075000028
     10: 50000085000036
     11: 50000095000045
     15: 50000135000091
     12: 50000105000055
     16: 50000145000105
     17: 50000155000120
     18: 50000165000136
     20: 50000185000171
     14: 50000125000078
    ...
     97: 50000955004560
    100: 50000985004851
     96: 50000945004465
     92: 50000905004095
     99: 50000975004753
*/

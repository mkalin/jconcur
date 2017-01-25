package basicC;

// A BasicCounter increments a counter a specified number of times, prints the
// value, and then terminates.
public class BasicCounter implements Runnable {
    private final long count;

    public BasicCounter(long count) { this.count = count; }

    @Override
    public void run() {
	long sum = 0;
	for (long i = 1; i < count; i++) sum += i;
	System.out.println(Thread.currentThread().getName() + ": " + sum);
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

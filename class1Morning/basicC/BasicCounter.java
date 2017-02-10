package basicC;

/** 
 * A BasicCounter increments a counter a specified number of times, prints the
 * value, and then terminates.
 *
 * In the series of three examples, a BasicCounter instance represents a job to be done.
 * The run() method, in executing, does the job.
 */
public class BasicCounter implements Runnable {
    private final long count; // how many times to iterate

    public BasicCounter(long count) { this.count = count; }

    @Override
    public void run() {
	long sum = 0; // local variables are per thread, hence thread-safe
	for (long i = 1; i < count; i++) sum += i;
	System.out.println(Thread.currentThread().getName() + ": " + sum); // trace each thread
    }
}


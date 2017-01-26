package basicC;

// A BasicCounter increments a counter a specified number of times, prints the
// value, and then terminates.
//
// In the series of three examples, a BasicCounter instance represents a job to be done.
// The run() method is the job.
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


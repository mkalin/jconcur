package basicC;

/** 
 * A BasicCounter increments a counter a specified number of times, prints the
 * value, and then terminates.
 *
 * In the series of three examples, a BasicCounter instance represents a job to be done.
 * The run() method, in executing, does the job.
 *
 * A programmer can create a thread in one of two basic ways, both of which require that
 * that a run() method be defined:
 *
 * -- create a class that extends java.lang.Thread (or a descendent class thereof) 
 *
 * -- implement the java.lang.Runnable interface, which the java.lang.Thread class does.
 *
 * This example takes the 2nd approach, but the 1st would work just as well.
 */
public class BasicCounter implements Runnable { // or extends Thread, if you prefer
    private final long count; // how many times to iterate

    public BasicCounter(long count) { this.count = count; }

    @Override
    public void run() {
	long sum = 0; // local variables are per thread, hence thread-safe
	for (long i = 1; i < count; i++) sum += i;
	System.out.println(Thread.currentThread().getName() + ": " + sum); // trace each thread
    }
}


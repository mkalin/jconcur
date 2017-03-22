package basicC;

/** 
 * A BasicCounter increments a counter a specified number of times, prints the
 * value, and then terminates.
 *
 * In the series of three examples, a BasicCounter instance represents a task to be done.
 * The run() method executes the task.
 *
 * A programmer can explicitly create a thread in one of two basic ways, both of which require 
 * that a run() method be defined:
 *
 * -- create a class that extends java.lang.Thread (or a descendant class thereof) 
 *
 * -- implement the java.lang.Runnable interface, which the java.lang.Thread class does.
 *
 * This example takes the 2nd approach, but the 1st would work just as well here.
 */
public class BasicCounter implements Runnable { // or extends Thread, if you prefer
    private final long count; // how many times to iterate

    public BasicCounter(long count) { this.count = count; }

    @Override
    public void run() {
	long sum = 0; // local variables are per thread, hence thread-safe
	for (long i = 1; i < count; i++) 
	    sum += i;
	System.out.println(Thread.currentThread().getName() + ": " + sum); // trace each thread
    } // The thread terminates when it exits run, and cannot be restarted.
}

/** Programming exercise/experiment:
 *
 * 1. Confirm that "implements Runnable" could be replaced by "extends Thread" with no other changes.
 *    Recompile and run to show that this change is inessential.
 *
 * 2. After replacing "implements Runnable" with "extends Thread", remove the override of the run()
 *    method. Compile and run the application to confirm that the app exits immediately.
 */


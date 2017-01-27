/* 
   The ThreadPool utility class is meant to allow a pool of threads to be created 
   at once and then reused thereafter. In this way, the cost of a "new" 
   operation is amortized over the lifetime of the 
   pool. "new" operations (and the garbage collection that they later require)
   are expensive; hence, pooled resources are an efficiency mechanism.

   The ThreadPool class also introduces a new Java construct, which has become
   popular: the inner class. Note that my ThreadPool class contains a class 
   called PoolWorker, which is private; hence, the PoolWorker class is visible
   only within ThreadPool --- it is not accessible anywhere else.

   The key to understanding how ThreadPool works are the two synchronized blocks:
   one is the execute method in ThreadPool itself, the other is inside the run
   method in ThreadWorker.

   Finally, from a high level, the MyThread class used here for testing represents
   a job. In other words, what my DriverTP does is this:

         -- creates a ThreadPool of size 10

         -- requests that the ThreadPool then execute 64 MyThread jobs.

   The output indicates that exactly 10 Java threads from the ThreadPool 
   do the work (the numbers are 0 through 9).
*/

import java.util.LinkedList;

public class ThreadPool {
    public ThreadPool(int n) {
        queue = new LinkedList<Runnable>();
        threads = new PoolWorker[thread_count = n];
	
	for (PoolWorker pw : threads) {
            pw = new PoolWorker();
            pw.start();
        }
    }
    
    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);  
            queue.notify();     
        }
    }

    private final int thread_count;
    private final PoolWorker[ ] threads;
    private final LinkedList<Runnable> queue;
    
    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;
            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        }
			catch (InterruptedException ignored) { }
                    }
                    r = (Runnable) queue.removeFirst();
                }
                try {
                    r.run(); // run the job
                }
                catch (RuntimeException e) {
                    log_exception(e);
                }
            }
        }
	private void log_exception(Exception e) { }
    }
}

/* A Job simply implements Runnable (the run() method). The actual work
   done can be anything you like.
*/
class Job implements Runnable {
    Job(String name) { setName(name); }
    void setName(String name) { this.name = name; }
    String getName() { return this.name; }

    // Sample (and very simple) Job example.
    public void run() {
	int i = 0; // per thread, hence thread safe
	while (true) {
	    System.out.println(getName() + " executing.");
	    i++;
	    if (i > max_runs) break; // forces an exit of run, which stops the thread
	    try {
		if (new java.util.Random().nextInt() < 0)
		    Thread.sleep(10);
		else
		    Thread.sleep(15);
	    } catch(InterruptedException e) { }
	}
    }
    String name;
    final int max_runs = 8;
}

// Program must be terminated at the command line or will
// continue indefinitely.
class DriverTP {
    public static void main(String[ ] args) throws Exception {
	// A pool of 10 threads to handle 64 Jobs.
	ThreadPool tp = new ThreadPool(10);
	for (int i = 0; i < 64; i++) 
	    tp.execute(new Job("Job" + i));
    }
}

/* output:
...
Job15 executing.
Job6 executing.
Job17 executing.
Job11 executing.
Job18 executing.
Job3 executing.
Job19 executing.
Job12 executing.
Job10 executing.
Job13 executing.
Job14 executing.
Job16 executing.
Job15 executing.
Job17 executing.
Job11 executing.
Job18 executing.
...
*/

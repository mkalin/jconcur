package tpool;

/** 
 * Any job submitted to the thread pool must implement Runnable by defining the run() method:
 *
 *    public MyJob implements Runnable { 
 *       public MyJob(String name) { ... }
 *
 *       @Override
 *  	 public void run() { ... } // run implements whatever logic the job requires
 *       ...
 *    }
 *
 * A job may be submitted for exeucution to a thread pool of N threads:
 *
 *    MyJob job1 = new MyJob("play a game");
 *    MyJob job2 = new MyJob("compress the database");
 *    ...
 *    ThreadPool tpool = new ThreadPool(8); // 8 threads in the pool
 *    tpool.execute(job1); // the tpool now provides the thread
 *    tpool.execute(job2); // ditto
 *    ...
 *
 * For ReentrantLocks, as against synchronized blocks, the wait/notify methods are
 * bundled into the java.util.concurrent.locks.Condition interface. Accordingly, this
 * thread pool implementation would replace the wait/notify constructs with
 * their Condition counterparts await/signal, if ReentrantLocks were used in place of
 * synchronized blocks.
*/
import java.util.LinkedList;

// Construct a pool and then use the pool to execute Runnable jobs.
final public class ThreadPool {
    private final int threadCount;               // size of thread pool
    private final PoolWorker[ ] poolWorkers;     // threads to do the work
    private final LinkedList<Runnable> jobQueue; // the work to be done

    public ThreadPool(int n) {
        jobQueue = new LinkedList<Runnable>();
        poolWorkers = new PoolWorker[this.threadCount = n];
	
	// Create and start the threads, amortizing the cost over the
	// lifetime of the pool.
	for (PoolWorker pw : poolWorkers) {
            pw = new PoolWorker();
            pw.start();
        }
    }
    
    // API method: a ThreadPool client creates a pool, and then 
    // invokes execute on a job to be run.
    //
    // The notify() method (encapsulated in Object, not Thread) can
    // be invoked only within a synchronized block: the compiler will
    // complain otherwise. The same holds for the related notifyAll()
    // method, and the overloaded wait(...) method used later.
    public void execute(Runnable job) {
        synchronized(jobQueue) {   // synchronized to prevent simultaneous adds/removes from queue
            jobQueue.addLast(job); // add the job to the end of the queue
            jobQueue.notify();     // wake up a thread to handle a job
        }
    }

    // PoolWorkers are Threads that are awaken from a wait-state (through a call to notify)
    // in order to handle the 1st job in the job queue. Once the job is completed, the
    // worker either returns to the wait-state, if there are no pending jobs, or handles
    // the next job in the queue, if there are pending jobs.
    private class PoolWorker extends Thread {
	@Override
        public void run() {
            Runnable job = null;

	    // A thread runs indefinitely within this 'infinite loop', waiting or
	    // working, as appropriate.
            while (true) {
		// The synchronization ensures that jobs are not simultaneously
		// added to and removed from the job queue. If there are no pending
		// jobs, a worker goes into an indefinite wait-state until awaken
		// to handle a newly added job.
		//
		// The overloaded wait(...) method, like the notify() and notifyAll()
		// methods, can be invoked only within a synchronized block.
                synchronized(jobQueue) {
                    while (jobQueue.isEmpty()) {
                        try {
			    jobQueue.wait(); // sleep until awoken
                        }
			catch (InterruptedException ignored) { }
                    }
                    job = (Runnable) jobQueue.removeFirst(); 
                } // end of synchronized block

                try {
                    job.run(); // run the job (outside the synchronized block)
                }
                catch (RuntimeException e) { }
            } // while (true)
        }
    }
}



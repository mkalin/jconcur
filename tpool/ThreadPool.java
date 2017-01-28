/** 
    Usage:

    Any job submitted to the thread pool must implement Runnable by defining the run() method:

    public MyJob implements Runnable { 
        public MyJob(String name) { ... }

        @Override
	public void run() { ... } // run implements whatever logic the job requires
        ...
    }

    A job then may be submitted for exeucution to a thread pool of N threads:

    MyJob job1 = new MyJob("play a game");
    MyJob job2 = new MyJob("compress the database");
    ...
    int howMany = 16; // 16 threads in the pool
    ThreadPool tpool = new ThreadPool(howMany);
    tpool.execute(job1);
    tpool.execute(job2);
    ...
*/
import java.util.LinkedList;

final public class ThreadPool {
    private final int threadCount;
    private final PoolWorker[ ] threads;
    private final LinkedList<Runnable> jobQueue;

    public ThreadPool(int n) {
        jobQueue = new LinkedList<Runnable>();
        threads = new PoolWorker[threadCount = n];
	
	for (PoolWorker pw : threads) {
            pw = new PoolWorker();
            pw.start();
        }
    }
    
    public void execute(Runnable r) {
        synchronized(jobQueue) {
            jobQueue.addLast(r);  // add the job to the queue
            jobQueue.notify();    // wake up a thread to handle the job
        }
    }
    
    private class PoolWorker extends Thread {
	@Override
        public void run() {
            Runnable job = null;
            while (true) {
                synchronized(jobQueue) {
                    while (jobQueue.isEmpty()) {
                        try {
                            jobQueue.wait();
                        }
			catch (InterruptedException ignored) { }
                    }
                    job = (Runnable) jobQueue.removeFirst();
                }
                try {
                    job.run(); // run the job
                }
                catch (RuntimeException e) { }
            }
        }
    }
}



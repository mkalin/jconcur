package tpool;

/* A Job simply implements Runnable (the run() method). The actual work
   done can be anything you like.
*/
class Job implements Runnable {
    String name;
    final int maxRuns = 8;

    Job(String name) { setName(name); }
    void setName(String name) { this.name = name; }
    String getName() { return this.name; }

    // Sample (and very simple) Job example.
    @Override
    public void run() {
	for (int i = 0; i < maxRuns; i++) {
	    System.out.println(getName() + " executing on " + Thread.currentThread().getName());

	    try {
		if (new java.util.Random().nextInt() < 0)
		    Thread.sleep(8);
		else
		    Thread.sleep(12);
	    } 
	    catch(InterruptedException e) { }
	}
    }
}

/** 
    Program must be terminated at the command line or will continue indefinitely:
    The 'main-thread' is a User (rather than a Daemon) thread, and any threads it creates
    are likewise User threads -- unless setDaemon(true) is called on the thread before it is
    started.

    A Java application runs so long as there is at least one User thread alive; the User threads
    in the ThreadPool are alive, although all of them are 'waiting' (sleeping efficiently) if there
    are no jobs to execute.
 */
class DriverTP {
    public static void main(String[ ] args) throws Exception {
	// A pool of 4 threads to handle 64 Jobs.
	ThreadPool tp = new ThreadPool(4);
	for (int i = 0; i < 64; i++) 
	    tp.execute(new Job("Job" + i));

	System.out.println("\n\n########## main-thread exiting...\n");
    }
}

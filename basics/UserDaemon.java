
/**
   A short application to illustrate User versus Daemon threads.

   -- A Java application remains alive so long as at least one User thread is alive.

   -- If a User thread creates a new thread, that thread is User by default.
      If a Daemon thread creates a new thread, that thread is Daemon by default.
      The status of a thread can be changed by invoking setDaemon(boolean) on the
      thread before the thread is started.

   -- The 'main-thread', which executes main, is a User thread.

   -- If the created thread in this example is a Daemon thread, the program exits when the
      main-thread exits main.

   -- If the created thread in this example is a User thread, the program runs until 
      killed externally (e.g., with Control-C from the command line).

  The JVM uses Daemon threads to provide various services, e.g., garbage-collection.
  Programmer apps can use any mix of User and Daemon threads -- great flexibility.
 */
public class UserDaemon {
    // A runtime-generated User thread executes main: the 'main-thread'.
    public static void main(String[ ] args) {
	System.out.println("main thread");

	Thread createdThread = new Thread() {
		// Sometime after a thread is started, it executes the run() method (and any
		// other code that run may be invoked within run). A started thread terminates
		// if the thread exits run.
		@Override
		public void run() {
		    System.out.println("created thread");            // if Daemon, may not execute
		    while (true) {
			try {
			    Thread.sleep(10); // sleep the current thread 10ms
			}
			catch(InterruptedException e) { }
			System.out.println("created thread awakes"); // if Daemon, may not execute
		    }
		}
	    };
	
	//### Run twice: once with the current setting, once with the line commented out.
	createdThread.setDaemon(true);  

	createdThread.start(); // start the thread
	
	System.out.println("main-thread terminating");
    } // main-thread terminates when it exits main
}
	    
	    

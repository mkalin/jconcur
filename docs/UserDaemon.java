
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

 */
public class UserDaemon {
    // A runtime-generated User thread executes main: the 'main-thread'.
    public static void main(String[ ] args) {
	System.out.println("main thread");

	Thread createdThread = new Thread() {
		@Override
		public void run() {
		    System.out.println("created thread");            // if Daemon, may not execute
		    while (true) {
			try {
			    Thread.sleep(10); // 10 ms
			}
			catch(InterruptedException e) { }
			System.out.println("created thread awakes"); // if Daemon, may not execute
		    }
		}
	    };
	
	// Run twice: once with the current setting, once with setDaemon(false) -- or the line commented out.
	createdThread.setDaemon(true);  

	createdThread.start(); // start the thread
	
	System.out.println("main-thread terminating");
    }
}
	    
	    

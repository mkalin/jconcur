import java.awt.Frame; // top-level window
import java.util.Set;

/**
   A program to show User and Daemon threads in action. The main-thread
   executes main, and is a User thread. This thread creates and shows an
   AWT Frame window, which results in a second User thread.

   The main-thread exits, but the second User thread persists -- and so does the application
   until killed externally (e.g., by Control-C from the command-line).
 */
public class MultithreadEx {
    public static void main(String[ ] args) {
	new MultithreadEx().demo();
	System.out.println("\nmain-thread is about to exit...");
    } //### main-thread terminates when it exits main()

    private void demo() {
	listThreads("Before Frame is shown...\n");

	// a new User thread will be created 
	new Frame("Just a demo, folks").setVisible(true);

	listThreads("\nAfter Frame is shown...\n");
    } 

    private void listThreads(String msg) {
	System.out.println(msg);

	// Each thread gets its own stack, so this is a straightforward 
	// (but not cheap) way to get a list of threads.
	Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	for (Thread t : threadSet) 
	    System.out.printf("\tName, Id, isDaemon(): %s (%d) %b\n",
			      t.getName(), t.getId(), t.isDaemon());
    }
}

/** 
    Output from a sample run (User threads marked with ###):

    Before Frame is shown...
    
        Name, Id, isDaemon(): Reference Handler (2) true
	Name, Id, isDaemon(): Signal Dispatcher (4) true
	Name, Id, isDaemon(): Finalizer (3) true
	Name, Id, isDaemon(): main (1) false                       ### User thread

    After Frame is shown...

	Name, Id, isDaemon(): Reference Handler (2) true
	Name, Id, isDaemon(): Java2D Disposer (10) true
	Name, Id, isDaemon(): Signal Dispatcher (4) true
	Name, Id, isDaemon(): Finalizer (3) true
	Name, Id, isDaemon(): main (1) false                       ### User thread
	Name, Id, isDaemon(): AWT-Shutdown (13) false              ### User thread
	Name, Id, isDaemon(): AWT-XAWT (12) true

    main-thread is about to exit...
*/

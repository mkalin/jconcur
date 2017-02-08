import java.awt.Frame; // top-level window
import java.util.Set;

/**
   A program to show User and Daemon threads in action. The main-thread
   executes main, and is a User thread. This thread creates and then shows an
   AWT Frame window: showing the window results in another User thread.

   The main-thread exits, but the second User thread persists -- and so does the application
   until killed externally (e.g., by Control-C from the command-line).
 */
public class MultithreadEx {
    public static void main(String[ ] args) {
	new MultithreadEx().demo();
	System.out.println("\nmain-thread is about to exit...");
    } //### main-thread terminates when it exits main()

    private void demo() {
	// Create and configure a top-level window.
	Frame win = new Frame("Hello, world!");
	win.setSize(600, 200);

	listThreads("Before Frame is shown...\n");
	win.setVisible(true); //### a new User thread is created
	listThreads("\nAfter Frame is shown...\n");
    } 

    private void listThreads(String msg) {
	System.out.println(msg);

	// Each thread gets its own stack scratchpad, so this is a straightforward 
	// (but not cheap) way to get a list of threads.
	Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	for (Thread t : threadSet) 
	    System.out.printf("\tName, Id, user/daemon: %s (%d) %s\n",
			      t.getName(), 
			      t.getId(), 
			      t.isDaemon() ? "Daemon" : "User");
    }
}

/** 
    Output from a sample run:

Before Frame is shown...

	Name, Id, user/daemon: Signal Dispatcher (4) Daemon
	Name, Id, user/daemon: main (1) User
	Name, Id, user/daemon: AWT-XAWT (12) Daemon
	Name, Id, user/daemon: Finalizer (3) Daemon
	Name, Id, user/daemon: Java2D Disposer (10) Daemon
	Name, Id, user/daemon: Reference Handler (2) Daemon

After Frame is shown...

	Name, Id, user/daemon: Signal Dispatcher (4) Daemon
	Name, Id, user/daemon: AWT-Shutdown (13) User
	Name, Id, user/daemon: Java2D Disposer (10) Daemon
	Name, Id, user/daemon: main (1) User
	Name, Id, user/daemon: AWT-XAWT (12) Daemon
	Name, Id, user/daemon: Finalizer (3) Daemon
	Name, Id, user/daemon: Reference Handler (2) Daemon

main-thread is about to exit...
*/

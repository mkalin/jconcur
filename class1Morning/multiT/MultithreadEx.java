package multiT;

import java.awt.Frame; // top-level window
import java.util.Set;

/**
   A program to show User and Daemon threads in action:

   Java has two Thread types: User and Daemon. Any Java thread of 
   execution is one or the other. From JDK 1.4 on, a Java thread should
   be implemented as a 'native' thread (under OS control) rather than as
   a 'green' thread (under JVM rather than OS control). Here's the 
   difference between a User and a Daemon thread:

   -- only a User thread can sustain an application, which thus ends if there
      are no more active User threads

   -- a Daemon thread is designed to be a 'background worker', a thread that does
      useful work but, by itself, cannot sustain an application

   In general, a Java application begins with the execution of main: the thread that
   executes main (the 'main-thread') is a User thread. A thread (User or Daemon) can 
   create and start other threads (User or Daemon). 

   In a technical sense, any Java application is multithreaded because the runtime (JVM)
   has various Daemon threads executing. Java multithreading becomes particularly 
   interesting and challenging when the programmer constructs, starts, and manages
   other threads.

   In this application, the main-thread creates and then shows an AWT Frame window.
   Although the number and type of threads generated is platform-specific, the
   creation and/or showing of the Frame window results in at least one additional
   User thread, with the main-thread as the original User thread.
   The main-thread exits, but the other User thread(s) associated with the Frame window
   remain. 

   To exit the application, hit Control-C from the command-line.
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
	win.setVisible(true);                         //### a new User thread is created
	listThreads("\nAfter Frame is shown...\n");
    } 

    private void listThreads(String msg) {
	System.out.println(msg);

	// Each thread gets its own stack scratchpad, so this is a straightforward 
	// (but not cheap) way to get a collection of all 'live' threads.
	Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	for (Thread t : threadSet) 
	    System.out.printf("\tName, Id, user/daemon: %s (%d) %s\n",
			      t.getName(), 
			      t.getId(), 
			      t.isDaemon() ? "Daemon" : "User");
    }
}

/** 
    Output from a sample run -- the output is platform-specific. This output
    comes from executing the program on a Linux (specifically, Ubuntu) machine.
    Expect slightly different output on other platforms.

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

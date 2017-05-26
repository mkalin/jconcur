package interrupt;

/**
 * A short program to illustrate how one thread (in this case, the main-thread)
 * can interrupt another thread (in this case, the Thread instance to which t1
 * points). Interruption is a primitive way for two threads to communicate.
 * 
 * In this example, the main-thread constructs and then starts a Thread instance,
 * whose run method contains a loop-until-interrupted construct. The main-thread
 * simulates a coin flip, incrementing a counter on 'tails' (negative values). When
 * the counter hits 5, the main-thread interrupts the started thread. Both
 * threads (each a User thread) then terminate, and the program exits. It's
 * arbitrary whether the main-thread or the started thread exits first.
 *
 */

import java.util.Random;

public class ThreadInterrupt {
    public static void main(String[ ] args) {
	new ThreadInterrupt().demo();
    }

    private void demo() {
	Thread t1 = new Thread() {
		// Run until interrupted by any other thread.
		@Override
		public void run() {
		    do {
			System.out.println("Started thread -- still alive and kicking...");
		    } while (!Thread.currentThread().interrupted());
		}
	    };

	// The main-thread is executing all of the following; hence, the main-thread
	// does the interrupting. Once started, t1 executes the run() method above.
	t1.start();

	Random rand = new Random();
	int count = 0;
	int limit = 4;

	while (true) {
	    // flip a coin: if tails (negative), increment count
	    if (rand.nextInt() < 0)
		count++;
	    // otherwise, sleep a little
	    else { 
		try {
		    Thread.sleep(5); // milliseconds
		}
		catch(Exception e) { }
	    }

	    // Check whether it's time to interrupt t1, and to 
	    // break out of this while (true) loop.
	    if (count == limit) {
		System.out.println("About to interrupt t1...");
		System.out.println("About to exit myself...");

		t1.interrupt(); // interrupt the explicitly started thread
		break;          // break out of the loop, and exit
	    }
	}
    }
}

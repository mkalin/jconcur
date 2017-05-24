package interrupt;

import java.util.Random;

public class ThreadInterrupt {
    public static void main(String[ ] args) {
	new ThreadInterrupt().demo();
    }
    private void demo() {
	Thread t1 = new Thread() {
		// started thread is executing the run method
		@Override
		public void run() {
		    do {
			System.out.println("Started thread -- still alive and kicking...");
		    } while (!Thread.currentThread().interrupted());
		}
	    };

	// main-thread is executing all of the following.
	t1.start();

	Random rand = new Random();
	int count = 0;
	int limit = 4;
	while (true) {
	    // flip a coin
	    if (rand.nextInt() < 0) // if negative, increment count
		count++;
	    else { // sleep the main-thread
		try {
		    Thread.sleep(5); // milliseconds
		}
		catch(Exception e) { }
	    }

	    if (count == limit) {
		System.out.println("About to interrupt t1...");
		System.out.println("About to exit myself...");
		t1.interrupt();
		break;
	    }
	}
    }
}

package bq;

/**
 * A program to make the Miser/Spendthrift competition thread-safe by using
 * a BlockingQueue to which each adds requests: the former to deposit, the
 * latter to withdraw. The ArrayBlockingQueue has a buffering capacity (it's a
 * "bounded buffer") so that multple requests can be pending at a time. 
 *
 * The application's primary threads are: 
 *
 *  -- the main-thread as usual
 *
 *  -- the Miser thread, which requests that the balance be incremented
 *
 *  -- the Sprendthrift thread, which requests that the balance be decremented
 *
 *  -- the Banker thread, which alone accesses the balance when handling requests
 *
 * So the Banker thread alone removes from the queue, and the Banker thread alone accesses 
 * the Account.balance, which prevents data races. The BlockingQueue itself is thread-safe.
 *
 * To simulate a more realistic scenario, the Miser and Spendthrift threads sleep a random
 * number of ticks after a deposit or withdrawal.
 *
 * There is a danger of deadlock in this application because 'read' operations on a
 * BlockingQueue do exactly what the name suggests: block, that is, wait until either there's 
 * something to read or there's a timeout. To avoid this danger, it's important that the
 * the application check that there's something in the queue before attempting a 'read'
 * operation.
 */

import java.util.concurrent.BlockingQueue;       // interface
import java.util.concurrent.ArrayBlockingQueue;  // a "bounded buffer" implementation
import java.util.Random;

class Miser extends Thread {       // deposit
    Miser(int howMany) { 
	this.howMany = howMany; 
	rand = new Random();
    }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) {
	    AccountBQ.bankQueue.add(new Integer(+1)); // deposit request
	    try {
		Thread.sleep(rand.nextInt(AccountBQ.zsCount)); // 0 to a few ms
	    }
	    catch(InterruptedException e) { }
	}
	System.out.println("Miser exiting");
    }
    
    private int howMany;
    private Random rand;
}

class Spendthrift extends Thread { // withdraw
    Spendthrift(int howMany) { 
	this.howMany = howMany; 
	rand = new Random();
    }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) {
	    AccountBQ.bankQueue.add(new Integer(-1)); // withdraw request
	    try {
		Thread.sleep(rand.nextInt(AccountBQ.zsCount)); // 0 to a few ms
	    }
	    catch(InterruptedException e) { }
	}
	System.out.println("Spendthrift exiting");
    }

    private int howMany;    
    private Random rand;
}

class Banker extends Thread {
    private Thread miser;
    private Thread spendthrift;

    Banker(Thread miser, Thread spendthrift) {
	this.miser = miser;
	this.spendthrift = spendthrift;
    }

    @Override
    public void run() {
	// Serve any customer that's still alive.
	while (miser.isAlive() || spendthrift.isAlive()) {
	    try {
		// If there's something in the queue, process it.
		// Note: Important not to block on the take() method
		// if there's nothing already in the queue -- both
		// 'writing' threads may have just terminated: deadlock.
		if (AccountBQ.bankQueue.peek() != null) {
		    Integer amt = AccountBQ.bankQueue.take(); // take() blocks
		    AccountBQ.balance += amt;
		}
	    }
	    catch(InterruptedException e) { }
	}
	System.out.println("Banker exiting");
    }
}

public class AccountBQ {
    public static final int zsCount = 3; // for Miser/Spendthrift sleeps
    public static int balance = 0;  

    // It's critical that the the queue be big enough to avoid a
    // 'full queue' expception, which would cause Miser and/or Spendthrift
    // requests to be lost. This size (1024) is a judgment call, but one based on experiment.
    // Making the queue capacity very small (8) did result in several exceptions,
    // but this size seems just fine; and a 1K array of object references is
    // relatively modest cost in memory.
    private static final int queueCapacity = 1024; 

    // 2nd ctor argument promotes 'fairness' between producing and consuming threads
    public static BlockingQueue<Integer> bankQueue = 
	new ArrayBlockingQueue<Integer>(queueCapacity, true); 
}

/** Review and research question on synchronization:
 *
 * In many of the examples given so far, print statements have been used to track
 * an application's behavior. In the current example, there are print statements to 
 * record when the Miser, Spendthrift, and Banker threads are about to exit. 
 * In mulithreaded Java apps, print statements typically don't occur within a synchronized block, 
 * which raises the question about whether methods such as println(...) have 'baked-in' synchronization.
 *
 * To make the issue more concrete, consider two print statements, each executing in
 * a separate thread on a multiprocessor machine: truly parallel execution is thus
 * possible. Here are the statements:
 *
 *     System.out.println("foo");   // executes in thread t1
 *     System.out.println("bar");   // executes in thread t2
 *
 * At issue is whether the output could be interleaved, for example:
 *
 *     fobaor
 *
 * In different terms, is there a possible race condition as each thread tries to
 * write its string to the standard output?
 */

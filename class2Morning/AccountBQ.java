/**
 * A program to make the Miser/Spendthrift competition thread-safe by using
 * a BlockingQueue to which each adds requests: the former to deposit, the
 * latter to withdraw. The ArrayBlockingQueue has a buffering capacity (it's a
 * "bounded buffer") so that multple requests can be pending at a time. 
 *
 * The Banker thread alone removes from the queue, and the Banker thread alone accesses 
 * the Account.balance, which prevents data races. The BlockingQueue itself is thread-safe.
 *
 * To simulate a more realistic scenario, the Miser and Spendthrift threads sleep a random
 * number of ticks after a deposit or withdrawal.
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
	    AccountBQ.bankQueue.add(new Integer(+1)); // deposit
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
	    AccountBQ.bankQueue.add(new Integer(-1)); // withdraw
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
		// threads may terminate in the meantime: deadlock.
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

package syncREL; // REL for 'ReentrantLock'

/**
 * Another synchronized version of Miser/Spendthrift: there's mutual exclusion on
 * updating the balance. As a result, the balance always winds up as it should: zero.
 *
 * A ReentrantLock has the same 'mutex' semantics as a synchronized block, but a richer
 * and more flexible API. In particular, there are various 'inspection' methods for determining
 * the current state of a Reentrant lock; also, a ReentrantLock can be configured so that
 * interrupting the thread that holds the lock causes that thread to release the lock.
 *
 */

import java.util.concurrent.locks.ReentrantLock; // see also the ReadLock and WriteLock variants

class Miser extends Thread {       // deposit
    Miser(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) {
	    // This style -- lock, try block for critical section, and
	    // finally block to unlock -- is considered sound practice; but there's
	    // lots of flexibility in the API, which we'll discuss.
	    AccountSyncREL.lock.lock();
	    try {
		AccountSyncREL.balance++;  // critical section code
	    }
	    finally {
		AccountSyncREL.lock.unlock();
	    }

	}
    }

    private int howMany;
}

class Spendthrift extends Thread { // withdraw
    Spendthrift(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) { 
	    AccountSyncREL.lock.lock();
	    try {
		AccountSyncREL.balance--;  // critical section code
	    }
	    finally {
		AccountSyncREL.lock.unlock();
	    }
	}
    }

    private int howMany;          
}

public class AccountSyncREL {
    public static int balance = 0;                             
    public static final ReentrantLock lock = new ReentrantLock(); 
}

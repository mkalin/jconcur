package sync;

/**
 * A synchronized version of Miser/Spendthrift: there's mutual exclusion on
 * updating the balance. As a result, the balance always winds up as it should: zero.
 *
 */

class Miser extends Thread {       // deposit
    Miser(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    synchronized(AccountSync.lock) {   // synchronized block
		AccountSync.balance++;         // thead-safe because of mutual exclusion
	    }
    }

    private int howMany;
}

class Spendthrift extends Thread { // withdraw
    Spendthrift(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    synchronized(AccountSync.lock) {  // synchronized block
		AccountSync.balance--;        // thread-safe because of mutual exclusion
	    }
    }

    private int howMany;          
}

public class AccountSync {
    public static int balance = 0;                             
    public static final Object lock = new Object(); // lock must not be null
}

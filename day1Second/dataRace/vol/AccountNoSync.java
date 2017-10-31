package vol;

class Miser extends Thread {       // deposit
    Miser(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    AccountNoSync.balance++;  // no synchronization on write
    }

    private int howMany;
}

class Spendthrift extends Thread { // withdraw
    Spendthrift(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    AccountNoSync.balance--;  // no synchronization on write
    }

    private int howMany;          
}

/** The 'volatile' qualifier does _not_ provide thread-safety on multiple operations
    such as reading-and-then-updating a variable. In short, this version of the
    program is still susceptible to a race condition because each thread does
    a read-and-then-update pair of operations on the balance.
**/
public class AccountNoSync {
    public static volatile int balance = 0; //## volatile does _not_ help
}

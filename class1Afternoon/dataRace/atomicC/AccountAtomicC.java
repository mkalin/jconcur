package atomicC;

/**
 * Of particular interest in this example is that the methods
 * incrementAndGet() and decrementAndGet() are synchronized on the _same_
 * lock, a particular AtomicInteger instance, in this case
 * AtomicInteger.balance: in effect, then, the two methods are
 * synchronized on 'this'. The result is that only one of these methods can
 * execute at a time on the balance object -- mutual exclusion is thereby achieved.
 *
 * The 'atomic' in the package name signals that operations such as 
 * incrementing and decrementing occur 'atomically', that is, without interruption.
 * For example, a thread that increments an AtomicInteger cannot be interrupted 'half way'
 * through, thereby raising the specter of a race condition.
 */
import java.util.concurrent.atomic.AtomicInteger; // thread-safety is built-in

class Miser extends Thread {       // deposit
    private int howMany;

    Miser(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    AccountAtomicC.balance.incrementAndGet(); // no explicit synchronization needed
    }
}

class Spendthrift extends Thread { // withdraw
    private int howMany;          

    Spendthrift(int howMany) { this.howMany = howMany; }

    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    AccountAtomicC.balance.decrementAndGet(); // no explicit synchronization needed
    }
}

public class AccountAtomicC {
    public static AtomicInteger balance = new AtomicInteger();  // one instance of the balance                       
}

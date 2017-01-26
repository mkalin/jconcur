package atomicC;

import java.util.concurrent.atomic.AtomicInteger;

class Miser extends Thread {       // deposit
    Miser(int howMany) { this.howMany = howMany; }
    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    AccountAtomicC.balance.incrementAndGet();
    }
    private int howMany;
}

class Spendthrift extends Thread { // withdraw
    Spendthrift(int howMany) { this.howMany = howMany; }
    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) 
	    AccountAtomicC.balance.decrementAndGet();
    }
    private int howMany;          
}

public class AccountAtomicC {
    public static AtomicInteger balance = new AtomicInteger();                            
}

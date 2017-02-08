package nosync;

class Miser extends Thread {       // deposit
    Miser(int howMany) { this.howMany = howMany; }
    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) AccountNoSync.balance++;   
    }
    private int howMany;
}

class Spendthrift extends Thread { // withdraw
    Spendthrift(int howMany) { this.howMany = howMany; }
    @Override
    public void run() {
	for (int i = 0; i < howMany; i++) AccountNoSync.balance--;   
    }
    private int howMany;          
}

public class AccountNoSync {
    public static int balance = 0; // exactly one account and its balance
}

package syncREL;

/**
   Miser/Spendthrift 2: two threads race to update a single memory location
   (static field AccountNoSync.balance): the Miser increments the balance, whereas
   the Spendthrift decrements the balance. The balance is zero to begin.

   In this version, there is explicit thread synchronization using the keyword
   'synchronized'. As a result, no data race occurs, and the balance at the end
   is what it should be: zero.
 */	

public class RaceConditionSyncREL {
    public static void main(String[ ] args) {
       if (args.length < 1) {
         System.err.println("RunCondition <times to iterate>");
         return;
       }

       int n = Integer.parseInt(args[0]);
       Miser miser = new Miser(n);       
       Spendthrift spendthrift = new Spendthrift(n);              

       miser.start();       // start Miser                        
       spendthrift.start(); // start Spendthrift */               

       try {                                                          
          miser.join();       // wait for Miser to terminate      
          spendthrift.join(); // wait for Spendthrift to terminate
       } catch(Exception e) { System.err.println(e); }

       System.out.println("Final balance: " + AccountSyncREL.balance); 
    }
}

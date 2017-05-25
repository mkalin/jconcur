package atomicC;

/**
   Miser/Spendthrift 3: two threads race to update a single memory location
   (static field AccountNoSync.balance): the Miser increments the balance, whereas
   the Spendthrift decrements the balance. The balance is zero to begin.

   In this version, an AtomicInteger is used to track the balance. The
   AtomicInteger has the thread synchronization baked in; hence, no explicit
   synchronization is needed.
 */
public class RaceConditionAtomicC {
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

       System.out.println("Final balance: " + AccountAtomicC.balance); 
    }
}

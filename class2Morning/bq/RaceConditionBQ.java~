/**
   Miser/Spendthrift 4: two threads race to update a single memory location
   (static field AccountBQ.balance): the Miser increments the balance, whereas
   the Spendthrift decrements the balance. The balance is zero to begin.

   In this version, there is an additional thread: the Banker thread.
   The Banker alone updates the account balance, with the Miser and Spendthrift 
   threads sending requests (for deposits and withdrawals, respectively) to
   a thread-safe BlockingQueue. The Banker thread reads requests from this queue.
   At the end, the balance is zero.

   Here's a depiction of how the app works:

               deposits
                 /                       thread-safe                updates the account balance
      Miser---------------+                 /                               /
                          |------------>BlockingQueue------------>Banker--------->balance
      Spendthrift---------+      \                      /                   /
                    \       write operations      read operations     single-threaded access
             withdrawals
 */	

public class RaceConditionBQ {
    public static void main(String[ ] args) {
       if (args.length < 1) {
         System.err.println("RunConditionBQ <times to iterate>");
         return;
       }

       int n = Integer.parseInt(args[0]);
       Miser miser = new Miser(n);       
       Spendthrift spendthrift = new Spendthrift(n);              
       Banker banker = new Banker(miser, spendthrift);

       miser.start();       // start Miser                        
       spendthrift.start(); // start Spendthrift      
       banker.start();      // start the Banker

       // The main-thread should be the last thread standing so that the main-thread
       // can print out the final balance _after_ the Miser and Spendthrift threads
       // have terminated.
       try {                                                          
	   banker.join(); // the Banker stays alive at least until the Miser and Spendthrift die
       } catch(Exception e) { System.err.println(e); }

       System.out.println("Final balance: " + AccountBQ.balance); 
    }
}

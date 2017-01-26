package nosync;

public class RaceConditionNoSync {
    public static void main(String[ ] args) {
       if (args.length < 1) {
         System.err.println("RunCondition <times to iterate>");
         return;
       }
       
       // Read in command-line argument: the number of times for each thread to do its thring
       int n = Integer.parseInt(args[0]);
       Miser miser = new Miser(n);                      // miser increments n times       
       Spendthrift spendthrift = new Spendthrift(n);    // spendthrift decements n times          

       // Start the two threads (the order is irrelevant).
       miser.start();       // start Miser                        
       spendthrift.start(); // start Spendthrift */      

       // Have the 'main thread' wait for the Miser and Spendthrift to terminate.
       try {                                                          
          miser.join();       // wait for Miser to terminate      
          spendthrift.join(); // wait for Spendthrift to terminate
       } catch(Exception e) { System.err.println(e); }

       // Print the final balance.
       System.out.println("Final balance: " + AccountNoSync.balance); 
    }
}

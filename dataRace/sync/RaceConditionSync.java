package sync;

public class RaceConditionSync {
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

       System.out.println("Final balance: " + AccountSync.balance); 
    }
}

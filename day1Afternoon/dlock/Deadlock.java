package dlock;

/**
   A program to illustrate deadlock. 

   Here's a scenario in which deadlock can occur, with clock ticks numbered T0, T1, T2,  
   and Bud and Lou as two friends who bow, and return a bow, as a formal greeting. Let 
   the thread that represents Lou be TL, and the thread representing Bud be TB. 

   In each Friend instance, the bow(...) and bowBack(...) methods are synchronized
   on that instance; hence, invoking bow(...) on the instance precludes
   calling bowBack(...) on the same instance -- until bow(...) has exited, thereby
   releasing the lock on bowBack(...).

   Here's a scenario that results in deadlock. The scenario is likely, but not certain: if
   you run the program, the program probably will deadlock.  The bow(...) method consists
   of two statements:

   -- First a print statement to the standard output. (Print statements are relatively slow.)
   -- Then an invocation of the Friend's bowBack(...) method.

   T0: TL starts executing the print statment in Lou's bow(...). Lou's bowBack(...) method
       is now locked as well because bow(...) and bowBack(...) are synchronized on the 
       same Friend instance ('this'), which is Lou.        

   T1: TL has not yet executed the bowBack(...) method called in bow(...), but TB is now executing
       the print statement in Bud's bow(...), which locks Bud's bowBack(...). In short,
       TL now cannot execute the bowBack(...) method called in bow(...).

   T2: TB finishes the print statement, but now cannot execute the bowBack(...) method with
       Lou as the instance because Lou's bowBack(...) method remains locked since T0.
        
   At this point, the bowBack(...) calls with Lou and Bud as the instances are blocked; they can become
   unblocked only if both calls proceed. Deadlock!

   Here's the program's likely output:

      Lou bows to Bud...
      Bud bows to Lou...

   If so, you'll have to terminate the program with a trusty control-C from the command-line prompt.

   Finally, it is possible for the program not to deadlock. Suppose that TL runs on a very fast
   processor, and TB on a very slow one. TL executes Lou's bow(...) and bowBack(...) before TB even begins 
   to execute Bud's bow(...). In this case, there's no deadlock.

   You might run the program several times to confirm. I'm betting imaginary money that the program 
   deadlocks every time.
 */
public class Deadlock {
    static class Friend {
        private final String name;

        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }

	// Only one of 'bow' and 'bowBack' can execute at a time, as they
	// are both locked on 'this' -- the current Friend instance.
        public synchronized void bow(Friend bower) {
            System.out.format("%s bows to %s...\n",
			      this.getName(), bower.getName());
            bower.bowBack(this);          //### executing bowBack would violate mutual exclusion
        } // lock is released only here

        public synchronized void bowBack(Friend bower) {
            System.out.format("%s bows back to %s...\n",
			      this.getName(), bower.getName());
        }
    }
    
    public static void main(String[ ] args) {
        final Friend lou = new Friend("Lou");
        final Friend bud = new Friend("Bud");

        new Thread(new Runnable() {
		@Override
		public void run() { 
		    lou.bow(bud); 
		}
	    }).start();

        new Thread(new Runnable() {
		@Override
		public void run() { 
		    bud.bow(lou); 
		}
	    }).start();
    }
}

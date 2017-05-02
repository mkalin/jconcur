package dlock;

/**
   A program to illustrate deadlock. Here's a scenario in which deadlock can occur, with
   the clock ticks numbered T0, T1,..., and Bud and Lou as two friends who bow, and return a
   bow, as a formal greeting. Let the thread that represents Lou be TL, and the thread
   representing Bud be TB. 

   In each Friend instance, the bow(...) and bowBack(...) methods are synchronized
   on the Friend instance; hence, invoking bow(...) on the instance precludes
   calling bowBack(...) on that instance -- until bow(...) has exited, thereby
   releasing the lock on bowBack(...).

   Here's a scenario that likely results in deadlock. The scenario is likely, but not certain: if
   you run the program, the program probably will deadlock. 

   T0: Thread TL, which the program starts first, execute's Lou's bow(...) method (with Bud as
       the argument), which thereby locks Lou's bowBack(...) method as well.

   T1: TL has not yet exited Lou's bow(...) method, and thread TB executes Bud's bow(...) method, 
       with Lou as the argument. Bud's bowBack(...) method is now locked as well.

   T2: TB has not yet exited Bud's bow(...) method, and thread TL tries to execute 
       Bud's bowBack(...) method -- but this method is locked because TB has not yet exited
       Bud's bow(...) method, which alone releases the lock.

   T3: Thread TB tries to execute Lou's bowBack(...) method, but it's locked as well because
       TL has not yet exited Lou's bow(...) method.

   At this point, thread TL is waiting for thread TB to release the lock on Bud's bowBack(...),
   but this release can occur only if thread TL can execute Bud's locked bowBack(...) method. The
   situation is the same with respect to TB: this thread is waiting for TL to release its
   lock on Lou's bowBack(...) method. 

   In summary, each thread now awaits the release of a lock, but such a release can occur only
   if each thread proceeds. The result is deadlock. Here's the likely output:

      Lou bows to Bud...
      Bud bows to Lou...

   Now the program needs to be terminated with the trusty Control-C from the command-line prompt.
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

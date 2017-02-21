package dlock;

/**
   A program to illustrate deadlock. Here's a summary of what happens:

   -- Two Friend instances are created, with references lou (name Lou) and bud (name Bud).

   -- Lou bows to Bud by way of greeting, so Bud should bow back to Lou.

   -- The Friend class has two synchronized methods, bow and bowBack. The implicit lock is 'this',
      a reference to the particular friend -- in this case, Lou or Bud.

   -- The bowBack method is invoked within the bow method; but the lock ensures mutual exclusion so
      that only either bow or bowBack can be run at a given time, not both at once.

   -- The result is deadlock: the call to bowBack, the last statement in bow, must wait until bow
      exits -- but bow can't exit until bowBack exits, and bowBack can't even begin until bow exits.

   -- The same thing happens when Bud bows to Lou.

   -- Yikes!
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
            System.out.format("%s: %s"
			      + "  has bowed to me!\n", 
			      this.getName(), bower.getName());
            bower.bowBack(this);          //### executing bowBack would violate mutual exclusion
        } // lock is released only here

        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s"
			      + " has bowed back to me!\n",
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

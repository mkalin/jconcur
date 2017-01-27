/**
   A program to illustrate deadlock. Here's a summary of what happens
   when the program runs:

   -- Two Friend instances are created, with references lou (name Lou) and bud (name Bud).

   -- Lou bows to Bud by way of greeting, and then Bud tries to bow back to Lou.

   -- The Friend class has two synchronized methods, bow and bowBack. The implicit lock is 'this',
      a reference to the particular friend.

   -- The bowBack method is invoked within the bow method; but the lock ensures mutual exclusion so
      that only _either_ bow _or_ bowBack can be invoked, not both at once by invoking one within
      the other.

   -- The result is deadlock: the call to bowBack, the last statement in bow, must wait until bow
      exits -- but bow can't exit until bowBack exits, and bowBack can't even begin until bow exits.

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

        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s"
			      + "  has bowed to me!%n", 
			      this.name, bower.getName());
            bower.bowBack(this);
        }

        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s"
			      + " has bowed back to me!%n",
			      this.name, bower.getName());
        }
    }
    
    public static void main(String[] args) {
        final Friend lou = new Friend("Lou");
        final Friend bud = new Friend("Bud");

        new Thread(new Runnable() {
		public void run() { lou.bow(bud); }
	    }).start();

        new Thread(new Runnable() {
		public void run() { bud.bow(lou); }
	    }).start();
    }
}

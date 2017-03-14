/** 
 * A short program to review data races:
 *
 *  -- Thread t1 sets fields 'a' and 'x', the latter to the value of 'b'
 *  -- Thread t2 sets fields 'b' and 'y', the latter to the value of 'a'
 *
 * These assignments could occur in different sequences, as there is
 * no synchronization of the writes done by the threads.
 */
public class DataRace {
    static int x = 0, y = 0;  // accessible to threads t1 and t2
    static int a = 0, b = 0;  // ditto

    public static void main(String[ ] args) throws InterruptedException {
	Thread t1 = new Thread() {
		@Override
		public void run() {
		    a = 1;          
		    x = b; // new b or original b?
		}
	    };

	Thread t2 = new Thread() {
		@Override
		public void run() {
		    b = 1;
		    y = a; // new a or original a?
		}
	    };	
	
	t1.start(); t2.start(); // start the two threads
	t1.join(); t2.join();   // forces main thread to wait

	System.out.println("x == " + x + "\n" +
			   "y == " + y + "\n" +
			   "a == " + a + "\n" +
			   "b == " + b);
    }
}

/* Output from a sample run:

   x == 0
   y == 1
   a == 1
   b == 1

   However, this scenario (among others) is possible: all are 1

   Time line =======...======>

        1st                 4th
         \                   /
   t1: a = 1...............x = b = 1

   t2:      b = 1....y = a = 1
              /        \
            2nd       3rd
*/

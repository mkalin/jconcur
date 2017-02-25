

-Djava.util.concurrent.ForkJoinPool.common.parallelism=16

http://blog.takipi.com/forkjoin-framework-vs-parallel-streams-vs-executorservice-the-ultimate-benchmark/

    approaches:

-- do it yourself

-- ExecutorService

-- ForkJoinFramework

-- Parallel streams


import java.util.stream.IntStream;
import java.util.Arrays;

public class RCond {
    private int[ ] nums;     // shared across two threads

    public static void main(String[ ] args) {
	new RCond().demo();
    }

    private void demo() {
	// Generate an array of integer values.
	nums = IntStream
	    .range(0, 2049)  // 0 through 2048
	    .toArray();      // arrayify the stream

	Thread t1 = new Thread() {
		@Override
		public void run() {

		}
	    };
	
	Thread t2 = new Thread() {
		@Override
		public void run() {

		}
	    };

	t1.start(); t2.start();

	// main-thread waits for t1 and t2 to complete their work.
	try {
	    t1.join(); t2.join();
	}
	catch (InterruptedException e) { }

	// Let's see what we get.
	for (int n : nums) System.out.println(n);
    }
}

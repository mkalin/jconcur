import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.concurrent.ForkJoinPool;

/**
 * Core Java 8 introduced two major APIs: one for lambdas ('anonymous functions'), 
 * method references, and constructor references; and another for streams. The two
 * are related in that lambdas and method references plug in naturally to 
 * streams as arguments to stream functions. The examples below illustrate.
 *
 * Our focus in on parallel streams, which provide a high-level and wonderfully low-fuss
 * way to do multithreading; indeed, the multithreading is basically 'automatic'. The
 * following short examples illustrate.
 *
 * For thread-safety, the lambdas in the following examples are all 'pure functions', that is,
 * functions whose return value depends exclusively on the argument(s) passed to the function.
 * The stream API's higher-level functions such as filter and map ('higher-level' in that these
 * functions take functions as arguments) encourage the use of pure functions: the simpler, the
 * better -- no side-effects, please!
 */

public class Intro2PStreams {
    public static void main(String[ ] args) {
	new Intro2PStreams().demo();
    }

    private void demo() {
	boolean printList = true; // set to false to turn off printing
	final int howMany = 1024;

	// Create some sample data, in this case 1024 int values.
	List<Integer> list = new ArrayList<Integer>();
	for (int i = 0; i < howMany; i++) list.add(i + 1);

	// warmup: a single-threaded version of collecting even numbers from a stream
	// In this example, 'filter' is an example of a higher-level function: a function
	// that takes a function (in this case, a lambda) as an argument.
	List <Integer> evens = 
	    list
	    .stream()                       // streamify the list: provide a 'conveyor belt' of values
	    .filter(n -> (n & 0x1) == 0)    // filter out not-even values
	    .collect(Collectors.toList());  // gather the values together in a list

	// Find out the number of worker threads available, typically the number of CPUs - 1.
	System.out.println("Pool size: " + 
			   ForkJoinPool.commonPool().getParallelism()); // 7 on this machine

	// go parallel
	List<Integer> odds = 
	    list
	    .parallelStream()               // scatter to different worker threads
	    .filter(n -> (n & 0x1) == 0)    // filter the same way again           
	    .map(n -> n + 1)                // odd successors (another higher-level function)
	    .collect(Collectors.toList());  // thread-safe gather

	if (printList) print(odds); // set printList to false to turn off printing

	// do a trace of the threads involved 
	list
	    .parallelStream()               // scatter
	    .filter(n -> (n & 0x1) == 0)               
	    .map(n -> n + 1)                // odd successors
	    .forEach(n -> System.out.format("%d (parallel) %s\n", // yet another higher-level function
					    n, 
					    Thread.currentThread().getName()));
	/* output from a sample run:
	   ...
	   107 (parallel) ForkJoinPool.commonPool-worker-6
	   69 (parallel) ForkJoinPool.commonPool-worker-7
	   57 (parallel) ForkJoinPool.commonPool-worker-5
	   17 (parallel) ForkJoinPool.commonPool-worker-3
	   195 (parallel) ForkJoinPool.commonPool-worker-2
	   223 (parallel) ForkJoinPool.commonPool-worker-1
	   197 (parallel) ForkJoinPool.commonPool-worker-2
	   19 (parallel) ForkJoinPool.commonPool-worker-3
	   59 (parallel) ForkJoinPool.commonPool-worker-5
	   61 (parallel) ForkJoinPool.commonPool-worker-5
	   71 (parallel) ForkJoinPool.commonPool-worker-7
	   109 (parallel) ForkJoinPool.commonPool-worker-6
	   207 (parallel) main
	   131 (parallel) ForkJoinPool.commonPool-worker-4
	   25 (parallel) main
	   ..
	 */
    }

    private void print(List<Integer> list) {
	System.out.println("Size: " + list.size());
	for (int n : list) System.out.println(n);
    }

}

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
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
 * The stream API's higher-order functions such as filter and map ('higher-order' in that these
 * functions take functions as arguments) encourage the use of pure functions: the simpler, the
 * better -- no side-effects, please!
 *
 * Finally, the program illustrates the 'scatter/gather' idiom: the problem's data are 'scattered' 
 * among different threads for processing, and then 'gathered' at the end into a list.
 */

public class Intro2PStreams {
    public static void main(String[ ] args) {
	new Intro2PStreams().demo();
    }

    private void demo() {
	boolean printList = true; // set to false to turn off printing
	final int howMany = 1024;

	// Create some sample data, in this case 1024 int values.
	List<Integer> list = new ArrayList<Integer>(); // empty list
	for (int i = 0; i < howMany; i++) list.add(i); // populate the list

	// Warmup: a single-threaded version of collecting even numbers from a stream
	// In this example, 'filter' is an example of a higher-order function: a function
	// that takes a function (in this case, a lambda) as an argument.
	List <Integer> evens = 
	    list
	    .stream()                       // streamify the list: provide a 'conveyor belt' of values
	    .filter(n -> (n & 0x1) == 0)    // filter out not-even values (filter is higher-order)
	    .collect(Collectors.toList());  // gather the values together in a list

	// Find out the number of worker threads available, typically the number of CPUs - 1.
	System.out.println("Pool size: " + 
			   ForkJoinPool.commonPool().getParallelism()); // 7 on this machine

	// Go parallel
	List<Integer> odds =                
	    list
	    .parallelStream()               // scatter to different worker threads
	    .filter(n -> (n & 0x1) == 0)    // filter the same way again           
	    .map(n -> n + 1)                // odd successors (map: another higher-order function)
	    .collect(Collectors.toList());  // thread-safe gather (but probably not in the original order)

	if (printList) print(odds); // set printList to false to turn off printing

	// Do a trace of the threads involved: parallelizing the stream means that the
	// runtime will partition the stream processing into multiple tasks, using the
	// ForkJoin framework under the hood to do so.
	list
	    .parallelStream()               // scatter
	    .filter(n -> (n & 0x1) == 0)    // even values only           
	    .map(n -> n + 1)                // odd successors
	    .forEach(n -> System.out.format("%d (parallel) %s\n", // forEach: yet another higher-order function
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

	// Can't count on the order in which the 'gather' happens.
	IntStream                            // integer stream
	    .range(1, howMany)               // generate a stream of int values
	    .parallel()                      // ### partition the data streams for parallel processing
	    .filter(i -> ((i & 0x1) > 0))    // odd parity? 
	    .forEach(System.out::println);   // print each
	/* Output slice:
	   ...
	   289
	   641
	   643
	   833
	   835
	   837
	   839
	   841
	   843
	   845
	   65
           929
           ...
	*/

	// A reduction example.
	Integer sum =
	    list
	    .parallelStream()
	    .reduce(0, (sofar, next) -> sofar + next);  // reduce is higher-order
	System.out.println("The sum is: " + sum);
	/* The 'reduce' operation above takes two args: 

	   -- 1st arg is the 'identity', in this case zero, serving as the initial value 
	   for the reduction and as the default value if the stream runs dry.
	   
	   -- 2nd arg is the 'accumulator', a lambda of two args: 1st is the reduction so far,
	   and the 2nd is the next value from the stream. The next value is added to the running sum
	   in this example.
	*/

	// Flattening a list of lists in parallel.
	List<List<Integer>> lol = new ArrayList<List<Integer>>();

	lol.add(Arrays.asList(1,2,3));
	lol.add(Arrays.asList(9,8,7,6,5));
	lol.add(Arrays.asList(-1,-2,-3,-4));

	System.out.println(lol);
	/* lol (list_of_lists) is: 

	   [[1, 2, 3], [9, 8, 7, 6, 5], [-1, -2, -3, -4]]
	 */
	List<Integer> flatList = 
	    lol
	    .parallelStream()
	    .flatMap(l -> l.stream())         // map each list to a stream of its elements
	    .collect(Collectors.toList());    // collect the elements into a single list
	System.out.println(flatList);         // [1, 2, 3, 9, 8, 7, 6, 5, -1, -2, -3, -4] on a sample run
    }

    private void print(List<Integer> list) {
	for (int n : list) System.out.println(n);
    }
}

/** Self-test issues for review
 *
 * The coding examples so far have illustrated four distinct approaches to multithreaded concurrent/parallel programming
 * in Java:
 *
 * -- do-it-yourlself multithreading, with the programmer constructing, starting, synchronizing, and otherwise managing
 *    the started threads
 *
 * -- mulithreading through the ExecutorService, with its built-in thread pools and thread management
 *
 * -- multithreading through the ForkJoin framework, which is built atop the ExecutorService and provides
 *    even higher-level support for concurrent/parallel programming
 *
 * -- multithreading through parallel streams, which are built atop the ForkJoin framework and provide
 *    high-level, 'automatic' concurrent/parallel support in the context of data streams
 *
 * As usual, there are tradeoffs. 
 *
 * http://blog.takipi.com/forkjoin-framework-vs-parallel-streams-vs-executorservice-the-ultimate-benchmark/
 *
 */

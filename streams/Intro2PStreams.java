import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Intro2PStreams {
    public static void main(String[ ] args) {
	new Intro2PStreams().demo();
    }

    private void demo() {
	boolean print = true;
	final int howMany = 256;
	List<Integer> list = new ArrayList<Integer>();    // collection to serve as a stream's source
	for (int i = 0; i < howMany; i++) 
	    list.add(i);                                  // populate the collection

	// warmup: a single-threaded version of collecting even numbers from a stream
	List<Integer> evens = 
	    list                                                                     
	    .stream()                       
	    .filter(n -> (n & 0x1) == 0)    // even nums only
	    .collect(Collectors.toList());  

	// go parallel
	List<Integer> odds = 
	    list                                                                     
	    .parallelStream()               // scatter
	    .filter(n -> (n & 0x1) == 0)               
	    .map(n -> n + 1)                // odd successors
	    .collect(Collectors.toList());  // thread-safe gather

	if (print) {
	    System.out.println("Size: " + odds.size());
	    for (int n : odds) {
		System.out.println(n);
	    }
	}

	// do a trace instead of a gather
	list                                                                     
	    .parallelStream()               // scatter
	    .filter(n -> (n & 0x1) == 0)               
	    .map(n -> n + 1)                // odd successors
	    .forEach(n -> System.out.format("%d (parallel) %s\n", 
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

}

/*
  Performing parallel operations on collections can lead to threading issues because the parallel operations
  may access shared storage (e.g., a field in some shared object) outside the streams, thereby raising 
  tread-safety issues that the programmer would have to address with the proper synchronization mechanisms.
  In short, parallel streams may make code thread-unsafe. Also, it's up to the programmer to determine
  whether the task at hand lends itself to parallelization in the first place.

  Java 8 uses parallel streams to partition streams into substreams, each of which executes on its own thread.
  Implementing parallelism on multi-core machines is now just a matter of invoking a parallel command on 
  the stream itself:

      list.stream().parallel()...
 
  or

      list.parallelStream()...

  At run-time, the thread-workers called into action with the 'parallel()' method come from the ForkJoinThreadPool, which the
  run-time maintains. The programmer does no explicit thread-creation. The size of the thread-pool depends, in general, on the
  number of processors available on the host system, and generally is set to one less than this number; however, the number is
  platform-specific -- you can't count on a particular number, but can set the thread-pool size when starting the program by using
  a flag:

  % java MyApp -Djava.util.concurrent.ForkJoinPool.common.parallelism=9

  There's a method, shown in the example, to discover the thread-pool size on a given platform.
*/

import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.List;

public class ParallelStreamOf {
    public static void main(String[ ] args) {
	new ParallelStreamOf().demo();
    }

    private void demo() {
	// "fork-join" means "scatter-gather": scatter the work to be done, gather the results together
	System.out.println("Default pool size: " + ForkJoinPool.commonPool().getParallelism()); // 7 on my machine

	IntStream                            // integer stream
	    .range(1, 1024)                  // generate a stream of int values: 1 through 1023
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
	   737
	   739
	   ...
	   765
	   767
	   819
	   821
	   823
	   825
	   827
	   829
	   513
	   515
	   517
	   519
	   521
	   523
	   525
	   527
	   529
	   531
	   533
	   535
	   537
	   539
	   541
	   831
	   543
	   ...
	 */
    }
}

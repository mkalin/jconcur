/* 
  The java.util.stream.Stream (interface) type declares methods suited for
  sequential and parallel operations on streams, where a stream is a sequence of
  elements. A stream commonly is described as a 'conveyor belt' of items, delivering
  the items one at a time for processing.

  The standard Java collections can be 'streamified', which in turn lends itself to
  stream-processing via lambdas. Java provides convenience methods such as StreamOf
  and IntStream that make it easy to generate streams (more of these two in later
  examples).
*/

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class StreamsReview {
    public static void main(String[ ] args) {	
	List<Integer> list = new ArrayList<Integer>();  // collection to serve as a stream's source
	for (int i = 0; i < 16; i++) list.add(i);       // populate the collection

	/** example 1: a sample 'data pipeline'  **/
	list                                                                                                       
	    .stream()                         // streamify the list: values are made available only 'on demand'
	    .map(n -> n + 1)                  // map each value to its successor (new stream created)
	    .forEach(System.out::println);    // print each of the successor values
	System.out.println();

	/** example 2 **/
	list                                                                     
	    .stream()
	    .filter(n -> (n & 0x1) == 0)                // even nums only
	    .map(n -> Arrays.asList(n - 1, n, n + 1))   // a more complicated mapping
	    .forEach(System.out::println);              // reference to a function
	System.out.println();

	/** example 3 **/
	list
	    .stream()
	    .map(n -> n + 1)
	    .forEach(n -> System.out.format("%d (sequential) %s\n", 
					    n, 
					    Thread.currentThread().getName()));
	System.out.println();

	/** example 4 **/
	list
	    .parallelStream()
	    .map(n -> n + 1)
	    .forEach(n -> System.out.format("%d (parallel) %s\n", 
					    n, 
					    Thread.currentThread().getName()));
	System.out.println();

	/** example 5 **/
	int intSum = 
	    list
	    .parallelStream()
	    .map(n -> n + 1)
	    // the 3 arguments are: 0 is the 'identity value' == the initial value of 
	    // the reduction and the default when no more stream values are available
	    // (sum, n) -> sum + n is the 'accumulator', 
	    // and (sum1, sum2) -> sum1 + sum2 is the 'combiner' function
	    .reduce(0, 
		    (sum, n) -> sum += n, 
		    (sum1, sum2) -> sum1 + sum2);
	System.out.println("\n### The sum: " + intSum);
	System.out.println();
	
	/** example 6 **/
	intSum = 
	    list
	    .parallelStream()
	    .map(n -> n + 1)
	    // the 3 arguments are: 0 is the 'identity value' == the initial value of 
	    // the reduction and the default when no more stream values are available
	    // (sum, n) -> sum + n is the 'accumulator', 
	    // and (sum1, sum2) -> sum1 + sum2 is the 'combiner' function
	    .reduce(0, 
		    (sum, n) -> { System.out.format("Accumulte: n (%d) and sum (%d)\n", n, sum); 
				  return sum += n; }, 
		    (sum1, sum2) -> { System.out.format("Combine: sum1 (%d) and sum2 (%d)\n", sum1, sum2);
				      return sum1 + sum2; });

	// list of lists
	System.out.println("\n");
	List<List<Integer>> lol = new ArrayList<List<Integer>>();
	lol.add(Arrays.asList(1,2,3));
	lol.add(Arrays.asList(9,8,7,6,5));
	lol.add(Arrays.asList(-1,-2,-3,-4));
	System.out.println(lol);
	/* lol (list_of_lists) is: 
	   [[1, 2, 3], [9, 8, 7, 6, 5], [-1, -2, -3, -4]]
	*/

	/* Here's a summy of how this next line works: 
	   
	   In the lambda expression that's the argument to flatMap, the argument n has 
	   one of the nested lists as its value: [1, 2, 3], [9, 8, 7, 6, 5], and [-1, -2, -3, -4].
	   The lamdba's body 'streamifies' each of these nested lists, and the subsequent call to
	   'collect' collects the streamed integers into a single, 'flattend' list.
	 */
	List<Integer> flatList = lol.parallelStream().flatMap(n -> n.stream()).collect(Collectors.toList());
	System.out.println(flatList);
	/* flattened list is:
	   [1, 2, 3, 9, 8, 7, 6, 5, -1, -2, -3, -4]
	 */
    }
}

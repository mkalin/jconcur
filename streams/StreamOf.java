import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.List;

public class StreamOf {
    public static void main(String[ ] args) {
	new StreamOf().demo();
    }
    private void demo() {
	Stream.of("ABBOTT", "COSTELLO",    // sequential stream
		  "ALLEN", "BURNS", 
		  "HAROLD", "KUMAR",
		  "BOB", "RAY",
		  "MAY", "NICHOLS")
	    .filter(s -> s.endsWith("Y"))  // filter by last letter
	    .map(String::toLowerCase)      // make sure we're not shouting
	    .sorted()                      // make sure everything's sorted
	    .forEach(System.out::println); // output: may ray

	IntStream                          // sequential integer stream: 1 through 64
	    .range(1, 65)                  // generate a stream of int values
	    .filter(i -> ((i & 0x1) > 0))  // odd parity?
	    .forEach(System.out::println); // print each

	List<String> stringifiedNums = 
	    IntStream
	    .range(1, 65)                  // generate a stream of int values
	    .filter(i -> ((i & 0x1) > 0))  // odd parity?
	    .mapToObj(n -> n + ": odd")
	    .collect(Collectors.toList()); // Collectors.toList() creates a 'Collector' instance
	stringifiedNums.stream().forEach(System.out::println);

	double avg  = 
	    IntStream
	    .range(1, 65)                  // generate a stream of int values
	    .filter(i -> ((i & 0x1) == 0))  // even parity?
	    .average()
	    .getAsDouble();
	System.out.println("The average is: " + avg);

	int sum  = 
	    IntStream
	    .range(1, 65)                  // generate a stream of int values
	    .filter(i -> ((i & 0x1) > 0))  // odd parity?
	    .sum();
	System.out.println("The sum is: " + sum);
    }
}

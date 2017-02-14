/** 
    Proof-of-concept examples: reducers and collectors 
 */

import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;

class Num {
    enum Parity {
        EVEN, ODD
    }
    Num(int n) { setValue(n); }
    private int value;
    public void setValue(int value) { this.value = value; }
    public int getValue() { return this.value; }
    public Parity getParity() {
	return ((value & 0x1) == 0) ? Parity.EVEN : Parity.ODD;
    }
    public void dump() {
	System.out.format("Value: %2d (parity: %s)\n", 
			  getValue(), 
			  (getParity() == Parity.ODD ? "odd" : "even"));
    }
}

public class GroupByEx {
    private static final int howMany = 64;

    public static void main(String[ ] args) {
	new GroupByEx().demo();
    }

    private void demo() {
	Random r = new Random();
	Num[ ] nums = new Num[howMany];
	for (int i = 0; i < howMany; i++) nums[i] = new Num(r.nextInt(100));
	List<Num> listOfNums = Arrays.asList(nums);  // listify the array

	Integer total4AllEasy =
	    listOfNums
	    .stream()
	    .mapToInt(Num::getValue)
	    .sum();                   //### reduce streamed values to a single value
	System.out.println("The sum of the randomly generated values is: " + total4AllEasy);

	Integer total4AllHarder =
	    listOfNums
	    .stream()
	    .map(Num::getValue)
	    .reduce(0, (sofar, next) -> sofar + next); 
	/** 'reduce' operation takes two args: 
	    -- 1st arg is the 'identity', serving as the initial value for the reduction and
	    as the default value if the stream runs dry.

	    -- 2nd arg is the 'accumulator', a lambda of two args: 1st is the reduction so far,
	    and the 2nd is the next value from the stream. The next value is added to the running sum
	    in this example.
	 */
	System.out.println("The sum again, using a different method:     " + total4AllHarder);
	
	/* In general, there are two similar methods associated with Collectors: reducers and collectors.
	   The primary difference lies in the names:

	   -- a reducer takes some existing value (e.g., a list of numbers)
	   and reduces it a new, single value (e.g., the average value of these numbers).

	   -- a collector accumulates or aggregates values as they arrive, thereby changing
	   the underlying 'collecting' structure (e.g., a list or a map)
	 */
	Map<Num.Parity, List<Num>> numMap = 
	    listOfNums
	    .stream()
	    .collect(Collectors.groupingBy(Num::getParity));

	List<Num> evens = numMap.get(Num.Parity.EVEN);
	List<Num> odds = numMap.get(Num.Parity.ODD);

	dumpList("Evens:\n", evens);
	System.out.println();
	dumpList("Odds:\n", odds);
    }
    
    private void dumpList(String msg, List<Num> list) {
	list.stream().forEach(n -> n.dump()); //## could be: forEach(Num::dump)
    }
}

/** The big picture:

    A 'data pipeline' often ends in a 'reduce' or a 'collect' operation:

       list.stream()...sum();         //*** reduce a list of nums to their sum

       list.stream()...collect(...);  //*** collect values into some data structure

    Collectors are powerful and flexible aggregators of data. This example offers one illustration.

    ------------------

    Here's an overview of the built-in libraries pertinent to 'collectors':

    java.util.stream.Collectors is a final class with built-in implementations of the java.util.stream.Collector
    interface. These implementations are suited for reducing streams to a value (e.g., summary values such as
    sums and averages for numeric data), collecting streamed values into data structures such as lists and
    maps, and grouping collected items into such data structures.

    The Collector interface, in turn, specifies four methods that an implementation must define:
    
    -- a supplier() function to supply values to be collected into a 'result container'

    -- an accumulator() to collect streamified values into a 'result container'

    -- a combiner() to combine two 'result containers'

    -- a finisher() to perform any required 'final transformation' of the 'result container'

    We've seen Collectors in action already with code such as  

        ...stream()...collect(Collections.toList())

    where Collections.toList() generates a Collector that knows how to 'listify' a stream's values.
    
    Programmers can roll their own Collectors, but the built-in collectors are rich and flexible already.
 */

import java.util.stream.Collectors;
import java.util.TreeSet;
import java.util.Set;
import java.util.Arrays;

class Item {
    private static int counter = 6;
    private int id;
    private String name;
    public Item() { 
	this.id = counter--;
	this.name = "Item-" + id;
    }
    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
}

public class TreeSetExer {
    public static void main(String[ ] args) {
	new TreeSetExer().demo();
    }
    
    /**
       The programming exercise:

       The code below collects streamified Item instances into a TreeSet, which sorts the items within the
       set. (Recall that a HashSet would not do any sorting.) 

       Another convenient 'collecting' structure is, of course, the map. The exercise is to generate a TreeMap 
       with an Item's name as the key and the Item itself as the value. There are various ways to do this; hence, 
       pick whatever approach works for you.
     */
    private void demo() {
	Item[ ] items = { new Item(), new Item(), new Item(), new Item(), new Item(), new Item() };

	Set<Integer> itemIds = 
	    Arrays.asList(items).stream().map(Item::getId).collect(Collectors.toCollection(TreeSet::new));

	itemIds.stream().forEach(System.out::println);
    }
}

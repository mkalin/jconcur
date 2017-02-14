import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.TreeSet;
import java.util.Set;
import java.util.Arrays;
import java.util.Map;

class Item {
    private static int counter = 0;
    private int id;
    private String name;
    public Item() { 
	this.id = ++counter; 
	this.name = "Item-" + id;
    }
    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
}

public class TreeSetExerAssess {
    public static void main(String[ ] args) {
	new TreeSetExerAssess().demo();
    }
    
    private void demo() {
	Item[ ] items = { new Item(), new Item(), new Item(), new Item(), new Item(), new Item() };
	Set<Integer> itemIds = 
	    Arrays
	    .asList(items)
	    .stream()
	    .map(Item::getId)
	    .collect(Collectors.toCollection(TreeSet::new));

	itemIds.stream().forEach(System.out::println);
	System.out.println();

	//*** Various ways this might be done: here's one.
	Map<String, Item> itemsMap =
	    Arrays
	    .asList(items)
	    .stream()
	    .collect(Collectors.toMap(item -> item.getName().toLowerCase(), // lower-cased name is the key
				      Function.identity()));                // Item itself is the value

	itemsMap.keySet().stream().forEach(System.out::println);
	System.out.println();
	itemsMap.values().stream().map(item -> item.getName()).forEach(System.out::println);
    }
}

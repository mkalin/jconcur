package streams;

/** 
 * A short program to underscore why lambdas should be 'pure functions',
 * that is, functions whose return value depends only on the arguments passed
 * to the function.
 *
 * In parallel streams, it's especially important that lambdas be pure functions: the
 * multithreading is 'automatic' in such streams, and it's easy to overlook the possibilities
 * for data races.
 *
 * Explain variables versus values: Java lambdas close over values, not variables
 *
 * Java lambdas are 'closures', functions that can 'close over' (in effect, incorporate) local 
 * ('lexically scoped') variables. However, the 'closed over' variables must be treated as read-only 
 * within a Java lambda, a restriction not imposed in every language. In effect, Java allows
 * the _values_ of lexically scoped variables to be 'closed over', but not the variables themselves.
 * This restriction promotes (but does not guarantee) lambdas as 'pure functions'. There's no
 * guarantee because a lambda still could perform 'write' operations on fields, which in turn could
 * be accessible across multiple threads.
 * 
 */

//### References to lambdas are of type FunctionalInterface, although the
//### anotation is optional.
@FunctionalInterface 
interface ClosureTestIface {
    // A reference of type ClosureTestIface would be used to invoked the count() method.
    abstract int count();  // abstract method: declared but not defined
}

public class Closures {
    int k = 0; // one copy per Closures instance: multithread access is possible

    public static void main(String[ ] args) {             
	new Closures().demo();
    }

    private void demo() {
	// The two functional references below would be invoked with this syntax:
	//
	//   autocounter1.count()
	//   autocounter2.count()
	//
	// The autocounter1 line below does not compile, however.

	/** The code below produces the following compiler-time error message:
	 *
	 * Closures.java:19: error: local variables referenced from a lambda expression must be final or effectively final
	 * ClosureTestIface autocounter1 = () -> ++n;   //## ERROR: n can be 'closed over', but must be read-only
	 *                                         ^
	 * 1 error
	 */
	int n = 0;                                      //## lexically scoped variable 
	ClosureTestIface autocounter1 = () -> ++n;      //## ERROR: n can be 'closed over', but must be read-only

	// compiles but not a good idea...
	ClosureTestIface autocounter2 = () -> ++k;      //## k is a field: thread-safety issues now arise
    }   
}


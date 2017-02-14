/** 
 * A short program to underscore why lambdas should be 'pure functions',
 * that is, functions whose return value depends only on the arguments passed
 * to the function.
 */

@FunctionalInterface 
interface ClosureTestIface {
    abstract int count();  // abstract method: declared but not defined
}

public class Closures {
    int k = 0; // one copy per Closures instance: multithread access is possible

    public static void main(String[ ] args) {             
	new Closures().demo();
    }

    private void demo() {
        int n = 0;                                    //## lexically scoped variable: 1 copy per thread

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
	ClosureTestIface autocounter1 = () -> ++n;    //## ERROR: n can be 'closed over', but must be read-only

	// compiles but not a good idea...
	ClosureTestIface autocounter2 = () -> ++k;    //## thread-safety issues now arise
    }   
}


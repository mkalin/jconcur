package fjt;
 
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
 
/**
 * A program to test the FileSearcher: search the user's home directory and any
 * subdirectories for files with the extensions 'txt', 'c', and 'java'.
 */

public class FileSearcherMain {
    private static final String startPath = System.getProperty("user.home");

    public static void main(String[ ] args) {
	new FileSearcherMain().demo();
    }

    private void demo() {
	ForkJoinPool pool = new ForkJoinPool(); // there's a Common Pool under the hood

	// Find out the targeted level of parallelism for the Common Pool, which is typically
	// the number of CPUs - 1. (There's also the convenience method getCommonPoolParallelism().).
	System.out.println("\nTargeted parallelism: " + 
			   ForkJoinPool.commonPool().getParallelism()); // 7 on this machine

	// Paths/extensions to be searched recursively. A FileSearch is a
	// RecursiveTask, as we'll see in the documentation for that class.
	FileSearcher text =  new FileSearcher(startPath, "txt");  // *.txt files
	FileSearcher capps = new FileSearcher(startPath, "c");    // *.c files
	FileSearcher java =  new FileSearcher(startPath, "java"); // *.java files
	
	// Add the three tasks to the pool for execution.
	pool.execute(text);  // 'execute' in this context means: do sometime in the future...
	pool.execute(capps);
	pool.execute(java);
	pool.shutdown();     // no more tasks will be submitted
	
	// Write info about the status of the pool every second
	// until the three tasks have finished their execution.
	do {
	    printReport(pool);
	    try {
		TimeUnit.SECONDS.sleep(1); // main-thread
	    } 
	    catch (InterruptedException e) {
		e.printStackTrace();
	    }
	} while ((!text.isDone()) || (!capps.isDone()) || (!java.isDone()));
	
	List<String> results = null;  // list of the files found in the search
	System.out.println();
	
	results = text.join();       // gather
	System.out.printf("Text:    %d files.\n", results.size());
	
	results = capps.join();      // gather
	System.out.printf("C apps:  %d files.\n", results.size());
	
	results = java.join();       // gather
	System.out.printf("Java:    %d files.\n", results.size());
    }

    private void printReport(ForkJoinPool pool) {
	System.out.printf("\n******************************************\n");
	System.out.printf("Pool size:      %d\n", 
			  pool.getPoolSize());           // worker threads not yet terminated
	System.out.printf("Active threads: %d\n", 
			  pool.getActiveThreadCount());  // estimated threads executing or stealing tasks 
	System.out.printf("Task count:     %d\n",     
			  pool.getQueuedTaskCount());    // estimated tasks queued up for threads
	System.out.printf("Steal count:    %d\n",    
			  pool.getStealCount());         // estimated tasks stolen from another thread's work queue
	System.out.printf("******************************************\n");
    }
}

/** Output from a sample run:

******************************************
Parallelism:    8
Active threads: 10
Task count:     120
Steal count:    4
******************************************

******************************************
Parallelism:    8
Active threads: 50
Task count:     1430
Steal count:    513
******************************************

******************************************
Parallelism:    8
Active threads: 12
Task count:     2
Steal count:    3108
******************************************

Text:    138 files.
C apps:  156 files.
Java:    132 files.

*/

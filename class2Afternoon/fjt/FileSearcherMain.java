package fjt;
 
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
 
/**
 * A program to test the FileSearcher: search the user's home directory and any
 * subdirectories for files with the extensions 'txt', 'c', and 'java'.
 *
 */

public class FileSearcherMain {
    private static final String startPath = System.getProperty("user.home");

    public static void main(String[ ] args) {
	new FileSearcherMain().demo();
    }

    private void demo() {
	/*
	  The default pool size is typically the number of processors (perhaps minus 1) on the local
	  system. The pool size also can set at the command-line with the following flag:
	  
	  java -Djava.util.concurrent.ForkJoinPool.common.parallelism=12 FileSearcherMain
	 */
	ForkJoinPool pool = new ForkJoinPool(); // a thread pool

	// Paths/extensions to be searched recursively. A FileSearch is a
	// RecursiveTask, as we'll see in the documentation for that class.
	FileSearcher text =  new FileSearcher(startPath, "txt");  // *.txt files
	FileSearcher capps = new FileSearcher(startPath, "c");    // *.c files
	FileSearcher java =  new FileSearcher(startPath, "java"); // *.java files
	
	// Add the three tasks to the pool for execution.
	pool.execute(text);
	pool.execute(capps);
	pool.execute(java);
	pool.shutdown();    // no more tasks
	
	// Write info about the status of the pool every second
	// until the three tasks have finished their execution.
	do {
	    System.out.printf("\n******************************************\n");
	    System.out.printf("Parallelism:    %d\n", 
			      pool.getParallelism());
	    System.out.printf("Active threads: %d\n", 
			      pool.getActiveThreadCount());
	    System.out.printf("Task count:     %d\n",     
			      pool.getQueuedTaskCount());
	    System.out.printf("Steal count:    %d\n",    
			      pool.getStealCount()); // tasks 'stolen' from another thread's work queue
	    System.out.printf("******************************************\n");
	    try {
		TimeUnit.SECONDS.sleep(1); // main-thread
	    } 
	    catch (InterruptedException e) {
		e.printStackTrace();
	    }
	} while ((!text.isDone()) || (!capps.isDone()) || (!java.isDone()));
	
	List<String> results;  // list of the files found in the search
	System.out.println();
	
	results = text.join();    
	System.out.printf("Text:    %d files.\n", results.size());
	
	results = capps.join();
	System.out.printf("C apps:  %d files.\n", results.size());
	
	results = java.join();
	System.out.printf("Java:    %d files.\n", results.size());
    }
}

/** Output from a sample run:

******************************************
Parallelism:    8
Active threads: 9
Task count:     176
Steal count:    31
******************************************

Text:    126 files.
C apps:  119 files.
Java:    121 files.

*/

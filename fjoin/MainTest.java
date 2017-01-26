package fjt;
 
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
 
public class Main {
   public static void main(String[] args) {
      ForkJoinPool pool = new ForkJoinPool();
      FileSearcher system = new FileSearcher("/home/kalin", "txt");
      FileSearcher apps = new FileSearcher("/home/kalin/", "c");
      FileSearcher documents = new FileSearcher("/home/kalin", "java");
      //Execute the three tasks in the pool using the execute() method.
      pool.execute(system);
      pool.execute(apps);
      pool.execute(documents);
      //Write to the console information about the status of the pool every second
      //until the three tasks have finished their execution.
      do {
         System.out.printf("\n******************************************\n");
         System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
         System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
         System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
         System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
         System.out.printf("******************************************\n");
         try {
            TimeUnit.SECONDS.sleep(1);
         } 
	 catch (InterruptedException e) {
	     e.printStackTrace();
         }
      } while ((!system.isDone()) || (!apps.isDone()) || (!documents.isDone()));
      pool.shutdown();
      List<String> results;
      results = system.join();
      System.out.println();
      System.out.printf("System:    %d files.\n", results.size());
      results = apps.join();
      System.out.printf("Apps:      %d files.\n", results.size());
      results = documents.join();
      System.out.printf("Documents: %d files.\n", results.size());
   }
}
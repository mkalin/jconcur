import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class ForkJoinDemo {
       public static void main(String[] args) throws InterruptedException, ExecutionException {
              ForkJoinPool fjp = new ForkJoinPool();
              DemoTask task = new DemoTask();
              ForkJoinTask<String>  fjt = ForkJoinTask.adapt(task);
              fjp.invoke(fjt);
              System.out.println(fjt.isDone());
       }
}
class DemoTask implements Callable<String>{
       public String call() {
              try {
                     Thread.sleep(1000);
              } catch (InterruptedException e) {
                     System.out.println(e);
              }
              return "Task Done";
       }
}
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ProducerConsumerDriver {
    public static void main(String[ ] args) {
        BlockingQueue<String> drop =
            new SynchronousQueue<String> ();
        (new Thread(new Producer(drop))).start();
        (new Thread(new Consumer(drop))).start();
    }
}
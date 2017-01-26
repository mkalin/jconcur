import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private BlockingQueue<String> drop;

    public Consumer(BlockingQueue<String> drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();
        try {
            for (String message = drop.take();
                 !message.equals("done");
                 message = drop.take()) {
                System.out.format("Message received: %s%n",
                                  message);
                Thread.sleep(random.nextInt(5000));
            }
        } catch (InterruptedException e) {}
    }
}

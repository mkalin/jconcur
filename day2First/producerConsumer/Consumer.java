package producerConsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private BlockingQueue<String> dropbox;

    public Consumer(BlockingQueue<String> dropbox) {
        this.dropbox = dropbox;
    }

    @Override
    public void run() {
        Random random = new Random();
	int k = 1;
        try {
            for (String message = dropbox.take(); 
		 !message.equals("done"); 
		 message = dropbox.take()) {
                System.out.format("Message %2d received: %s\n", k++, message);

                Thread.sleep(random.nextInt(4000));
            }
        } 
	catch (InterruptedException e) { }
    }
}

package producerConsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private BlockingQueue<String> dropbox;

    public Producer(BlockingQueue<String> dropbox) {
        this.dropbox = dropbox;
    }

    @Override
    public void run() {
        String importantInfo[ ] = {
            "Mares eat oats",
            "Does eat oats",
            "Little lambs eat ivy",
            "A kid will eat ivy too",
	    "now from Ella",
	    "A-tisket, a-tasket",
	    "A green and yellow basket",
	    "I wrote a letter to my love",
	    "And on the way I dropped it",
	    "I dropped it",
	    "Yes, on the way I dropped it",
	    "A little boy he picked it up",
	    "and put it in his pocket."
        };

	final int howMany = 24;
        Random random = new Random();
        try {
            for (int i = 0; i < howMany; i++) {
		int ind = random.nextInt(importantInfo.length);
                dropbox.put(importantInfo[ind]);

                Thread.sleep(random.nextInt(4000)); // a short pause for realism
            }
            dropbox.put("done");
        } 
	catch (InterruptedException e) { }
    }
}

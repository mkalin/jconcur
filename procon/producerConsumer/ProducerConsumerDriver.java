package producerConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/** 
 * Provide a thread-safe queue for a Producer and Consumer to communicate.
 * The SynchronousQueue is a 'blocking' structure that coordinates insert ('write')
 * and remove ('read') operations: each insert requires a subsequent remove, and each
 * remove depends on a previous insert. Although the term 'queue' suggests a collection of
 * multiple items, the SynchronousQueue is, at the implementation level, simply a mechanism
 * that allows a producer and a consumer share items strictly one-at-a-time, but in a thread-safe
 * manner: one thread can produce, and another consume. In short, a SynchronousQueue does not have
 * a size or a capacity.
 */
public class ProducerConsumerDriver {
    public static void main(String[ ] args) {
        BlockingQueue<String> dropbox = new SynchronousQueue<String>(); // thread-safe

        (new Thread(new Producer(dropbox))).start(); // start the producer
        (new Thread(new Consumer(dropbox))).start(); // start the consumder
    }
}
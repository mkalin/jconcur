package producerConsumer;

import java.util.concurrent.BlockingQueue;     // interface
import java.util.concurrent.SynchronousQueue;  // one implementation

/** 
 * Provide a thread-safe queue for a Producer and Consumer to communicate.
 * The SynchronousQueue is a 'blocking' structure that coordinates insert ('write')
 * and remove ('read') operations: each insert requires a subsequent remove, and each
 * remove depends on a previous insert. Although the term 'queue' suggests a collection of
 * multiple items, the SynchronousQueue is, at the implementation level, simply a mechanism
 * that allows a producer and a consumer share items strictly one-at-a-time, but in a thread-safe
 * manner: one thread can produce, and another consume. In short, a SynchronousQueue does not have
 * a size or a capacity.
 *
 * There are three User threads at play: the main-thread in ProducerConsumerDriver (this file),
 * the producer thread, and the consumer thread.
 *
 * Here's a depiction of the application:
 * 
 *         a write of "done" signals the Producer is finished producing
 *               /
 *            write                        read
 *  Producer---------->SynchronousQueue<----------Consumer
 *                          /                \
 *                    thread-safe       read until "done" is encountered             
 *
 */
public class ProducerConsumerDriver {
    public static void main(String[ ] args) {
        BlockingQueue<String> dropbox = new SynchronousQueue<String>(); // thread-safe

        (new Thread(new Producer(dropbox))).start(); // start the producer
        (new Thread(new Consumer(dropbox))).start(); // start the consumder
    }
}


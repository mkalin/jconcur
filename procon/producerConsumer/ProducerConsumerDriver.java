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

/** Self-test exercise

    Implementations of the BlockingQueue interface suggest another way to make
    the Miser/Spendthrift program (see class1Afternoon.zip) thread-safe. In the
    original versions, the Miser and the Spendthrift have direct access to the
    Account.balance, which thus must be synchronized in some way (explicitly or
    with a thread-safe type such as the AtomicInteger) in order to prevent a data
    race. Another option is to introduce a banker into the mix:

                 desposit       +----------+
          Miser---------------->|  Banker  |--------->balance
    Spendthrift---------------->|  thread  |
                 withdraw       +----------+

   The Banker thread alone would have direct access to the balance; the Miser and
   Spendthrift queues would add deposit and withdraw requests, respectively, to a
   BlockingQueue that the Banker thread alone reads.

   Code up this revised version of the Miser/Spendthrift program, using whatever
   implementation of BlockingQueue you like. Confirm through sample runs that the
   application works correctly.
 */

package basicC;

// A CallableBasicCounter implements the Callable<Long> interface,
// which means that the overridden call method below must return a Long (or long).
import java.util.concurrent.Callable;

public class CallableBasicCounter implements Callable<Long> {
    private final long count;

    public CallableBasicCounter(long count) { this.count = count; }

    @Override
    public Long call() throws Exception {
	long sum = 0;

	for (long i = 0; i < count; i++) sum += i;
	System.out.println(Thread.currentThread().getName() + ": " + sum);
	return sum;
    }
}

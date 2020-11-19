package increment;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeIncrementor implements Incrementor {

    private AtomicInteger value = new AtomicInteger(0);

    @Override
    public int increment() {
        return value.incrementAndGet();
    }
}

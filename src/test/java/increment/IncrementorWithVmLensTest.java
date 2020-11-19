package increment;

import com.vmlens.api.AllInterleavings;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class IncrementorWithVmLensTest {

    @Test
    public void testIncrement() throws InterruptedException {
        var incrementor = new VolatileIncrementor();
        try (AllInterleavings allInterleavings =
                     new AllInterleavings("tests.IncrementorWithVmLens.Increment")) {
            while (allInterleavings.hasNext()) {
                final AtomicInteger firstResult = new AtomicInteger();
                final AtomicInteger secondResult = new AtomicInteger();
                Thread first = new Thread(() -> {
                    firstResult.set(incrementor.increment());
                });
                Thread second = new Thread(() -> {
                    secondResult.set(incrementor.increment());
                });
                first.start();
                second.start();
                first.join();
                second.join();
                assertNotEquals(firstResult.get(), secondResult.get());
            }
        }
    }


    @Test
    public void testWronglySynchronizedIncrement() throws InterruptedException {
        var incrementor = new WronglySynchronizedncrementor();
        try (AllInterleavings allInterleavings =
                     new AllInterleavings("tests.IncrementorWithVmLens.WronglySynchronizedIncrement")) {
            while (allInterleavings.hasNext()) {
                final AtomicInteger firstResult = new AtomicInteger();
                final AtomicInteger secondResult = new AtomicInteger();
                Thread first = new Thread(() -> {
                    firstResult.set(incrementor.increment());
                });
                Thread second = new Thread(() -> {
                    secondResult.set(incrementor.increment());
                });
                first.start();
                second.start();
                first.join();
                second.join();
                assertNotEquals(firstResult.get(), secondResult.get());
            }
        }
    }

}

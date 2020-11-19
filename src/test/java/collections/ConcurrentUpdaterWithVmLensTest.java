package collections;

import com.vmlens.api.AllInterleavings;
import increment.VolatileIncrementor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentUpdaterWithVmLensTest {

    @Test
    public void testIncrement() throws InterruptedException {
        var updater = new ConcurrentUpdater();
        try (AllInterleavings allInterleavings =
                     new AllInterleavings("tests.ConcurrentUpdaterWithVmLensTest")) {
            while (allInterleavings.hasNext()) {
                Thread first = new Thread(() -> {
                    updater.putOrConcat("joe", "smith");
                });
                Thread second = new Thread(() -> {
                    updater.putOrConcat("joe", "smith");
                });
                first.start();
                second.start();
                first.join();
                second.join();
                assertEquals(updater.get("joe"), "smith smith");
            }
        }
    }

}

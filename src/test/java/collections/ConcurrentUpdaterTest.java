package collections;

import com.vmlens.api.AllInterleavings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcurrentUpdaterTest {

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

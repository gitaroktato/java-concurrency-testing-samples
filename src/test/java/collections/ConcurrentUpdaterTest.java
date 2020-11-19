package collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

import static concurrency.Test.runConcurrently;

public class ConcurrentUpdaterTest {

    private static final int THREADS = 20;
    private static final int CYCLES = 200;

    @RepeatedTest(5)
    public void testUpdate() throws InterruptedException {
        var subject = new ConcurrentUpdater();
        runConcurrently(THREADS, CYCLES, () -> {
            subject.putOrConcat("key", "value");
        });
        var result = subject.get("key");
        var values = result.split(" ");
        Assertions.assertEquals(THREADS * CYCLES, values.length);
    }

}

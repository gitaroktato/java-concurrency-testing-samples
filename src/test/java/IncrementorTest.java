import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IncrementorTest {

    public static final int CYCLES = 200;
    public static final int THREADS = 200;

    @Test
    public void testIncrement() throws InterruptedException {
        List<Integer> results = new CopyOnWriteArrayList<>();
        var subject = new Incrementor();
        runConcurrently(THREADS, CYCLES, () -> {
            var result = subject.increment();
            results.add(result);
        });
        var duplicateElements = collectDuplicates(results);
        Assertions.assertIterableEquals(Collections.emptyList(), duplicateElements);
    }

    private void runConcurrently(int treads, int cycles, Runnable criticalSection) {
        var barrier = new CyclicBarrier(treads);
        var pool = Executors.newFixedThreadPool(treads);
        IntStream.range(0, treads).forEach(i ->
                pool.submit(() -> {
                    try {
                        barrier.await();
                        IntStream.range(0, cycles).forEach(j -> criticalSection.run());
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        Assertions.fail(e);
                    }
                }));
        pool.shutdown();
        Assertions.assertTimeout(Duration.ofSeconds(10),
                () -> pool.awaitTermination(11, TimeUnit.SECONDS));
    }

    private List<Integer> collectDuplicates(List<Integer> results) {
        return results.stream()
                .filter(e -> Collections.frequency(results, e) > 1)
                .distinct()
                .collect(Collectors.toList());
    }

    // TODO BoundedBuffer ??
    // TODO volatile Incrementor?
    // TODO better abstraction of test phases?

}
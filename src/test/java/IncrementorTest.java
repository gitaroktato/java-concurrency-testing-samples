import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class IncrementorTest {

    public static final int CYCLES = 200;
    public static final int THREADS = 200;

    @Test
    public void testIncrement() throws InterruptedException {
        List<Integer> results = new CopyOnWriteArrayList<>();
        var subject = new Incrementor();
        var barrier = new CyclicBarrier(THREADS);
        var pool = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < THREADS; i++) {
            pool.submit(() -> {
                try {
                    barrier.await();
                    for (int j = 0; j < CYCLES; j++) {
                        var result = subject.increment();
                        results.add(result);
                    }
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
        Assertions.assertTimeout(Duration.ofSeconds(10),
                () -> pool.awaitTermination(11, TimeUnit.SECONDS));
        var duplicateElements = results.stream()
                .filter(e -> Collections.frequency(results, e) > 1)
                .distinct()
                .collect(Collectors.toList());
        Assertions.assertIterableEquals(Collections.emptyList(), duplicateElements);
    }

}
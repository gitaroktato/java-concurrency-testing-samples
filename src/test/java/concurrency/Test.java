package concurrency;

import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Test {

    public static void runConcurrently(int treads, int cycles, Runnable criticalSection) {
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
}

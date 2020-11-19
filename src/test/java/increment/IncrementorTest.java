package increment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static concurrency.Test.runConcurrently;

public class IncrementorTest {

    public static final int CYCLES = 200;
    public static final int THREADS = 20;

    private List<Integer> collectDuplicates(List<Integer> results) {
        return results.stream()
                .filter(e -> Collections.frequency(results, e) > 1)
                .distinct()
                .collect(Collectors.toList());
    }

    @RepeatedTest(5)
    public void testIncrementIsThreadSafe() throws InterruptedException {
        List<Integer> results = new CopyOnWriteArrayList<>();
        var subject = new SimpleIncrementor();
        runConcurrently(THREADS, CYCLES, () -> {
            var result = subject.increment();
            results.add(result);
        });
        var duplicateElements = collectDuplicates(results);
        Assertions.assertIterableEquals(Collections.emptyList(), duplicateElements);
    }


    @RepeatedTest(5)
    public void testVolatileIncrementIsThreadSafe() throws InterruptedException {
        List<Integer> results = new CopyOnWriteArrayList<>();
        var subject = new VolatileIncrementor();
        runConcurrently(THREADS, CYCLES, () -> {
            var result = subject.increment();
            results.add(result);
        });
        var duplicateElements = collectDuplicates(results);
        Assertions.assertIterableEquals(Collections.emptyList(), duplicateElements);
    }

    @RepeatedTest(5)
    public void testWronglySynchronizedIncrementIsThreadSafe() throws InterruptedException {
        List<Integer> results = new CopyOnWriteArrayList<>();
        var subject = new WronglySynchronizedncrementor();
        runConcurrently(THREADS, CYCLES, () -> {
            var result = subject.increment();
            results.add(result);
        });
        var duplicateElements = collectDuplicates(results);
        Assertions.assertIterableEquals(Collections.emptyList(), duplicateElements);
    }

}
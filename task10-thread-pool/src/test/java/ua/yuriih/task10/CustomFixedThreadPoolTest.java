package ua.yuriih.task10;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class CustomFixedThreadPoolTest {

    @Test
    void constructor() {
        assertDoesNotThrow(() -> new CustomFixedThreadPool(2));
        assertDoesNotThrow(() -> new CustomFixedThreadPool(1));
        assertThrows(IllegalArgumentException.class, () ->
                new CustomFixedThreadPool(0));
        assertThrows(IllegalArgumentException.class, () ->
                new CustomFixedThreadPool(-1));
    }

    @Test
    void execute_singleThread() throws InterruptedException {
        for (int i = 0; i < 10; i++)
            execute_do(5, 1, 50);
    }

    @Test
    void execute_multipleThreads() throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < 10; i++)
            execute_do(cores * 3, cores, 50);
    }

    @Test
    void execute_lotsOfThreads() throws InterruptedException {
        for (int i = 0; i < 10; i++)
            execute_do(5, 100, 50);
    }
    
    private void execute_do(int tasksCount, int threadsCount, int taskTimeMillis)
            throws InterruptedException {
        //How many times has a task been completed?
        //Should be equal to 1
        int[] tasksCompleted = new int[tasksCount];
        CustomFixedThreadPool threadPool = new CustomFixedThreadPool(threadsCount);

        for (int i = 0; i < tasksCount; i++) {
            final int iFinal = i;
            threadPool.execute(() -> {
                assertDoesNotThrow(() -> Thread.sleep(taskTimeMillis));
                tasksCompleted[iFinal]++;
            });
        }

        Thread.sleep(taskTimeMillis * tasksCount / threadsCount + 100);

        int[] tasksCompletedExpected = new int[tasksCount];
        Arrays.fill(tasksCompletedExpected, 1);
        assertArrayEquals(tasksCompletedExpected, tasksCompleted);
    }

    @Test
    void getQueueSize() throws InterruptedException {
        final int TASK_COUNT = 5;
        final int THREAD_COUNT = 2;

        CustomFixedThreadPool threadPool = new CustomFixedThreadPool(THREAD_COUNT);

        assertEquals(0, threadPool.getQueueSize());

        for (int i = 0; i < TASK_COUNT; i++) {
            final int iFinal = i;
            threadPool.execute(() -> {
                assertDoesNotThrow(() -> Thread.sleep(100));
            });
        }
        assertEquals(3, threadPool.getQueueSize());
        Thread.sleep(150);
        assertEquals(1, threadPool.getQueueSize());
        Thread.sleep(150);
        assertEquals(0, threadPool.getQueueSize());
    }
}
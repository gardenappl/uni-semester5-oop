package ua.yuriih.task7;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.font.TextHitInfo;
import java.nio.charset.IllegalCharsetNameException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class CustomCyclicBarrierTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCyclicBarrierTest.class);
    
    @Test
    void await_failAction() throws InterruptedException {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(2, () -> {
            LOGGER.info("Action called from {}", Thread.currentThread());
            throw new RuntimeException("Error in barrier action");
        });

        assertEquals(0, barrier.getNumberWaiting());


        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<?> future = service.submit(() -> {
            assertThrows(BrokenBarrierException.class, () -> barrier.await());
        });


        Thread.sleep(100);
        
        assertThrows(RuntimeException.class, () ->
                barrier.await(), "Error in barrier action");

        //join thread
        assertDoesNotThrow(() -> future.get());

        assertEquals(0, barrier.getNumberWaiting());
        assertTrue(barrier.isBroken());
    }

    @Test
    void await_notBroken() throws InterruptedException {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(3);

        assertEquals(0, barrier.getNumberWaiting());


        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<?> future1 = service.submit(() -> {
            assertDoesNotThrow(() -> Thread.sleep(2000));
            assertDoesNotThrow(() -> barrier.await());
        });
        Future<?> future2 = service.submit(() -> {
            assertDoesNotThrow(() -> Thread.sleep(1000));
            assertDoesNotThrow(() -> barrier.await());
        });


        assertEquals(0, barrier.getNumberWaiting());

        Thread.sleep(1500);
        assertEquals(1, barrier.getNumberWaiting());

        Thread.sleep(1500);
        assertEquals(2, barrier.getNumberWaiting());


        assertDoesNotThrow(() ->
            assertEquals(0, barrier.await())
        );

        //join threads
        assertDoesNotThrow(() -> future1.get());
        assertDoesNotThrow(() -> future2.get());


        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    void await_reset() throws InterruptedException {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(3);

        assertEquals(0, barrier.getNumberWaiting());


        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<?> future1 = service.submit(() -> {
            assertDoesNotThrow(() -> Thread.sleep(2000));
            assertThrows(BrokenBarrierException.class, () ->
                    barrier.await());
        });
        Future<?> future2 = service.submit(() -> {
            assertDoesNotThrow(() -> Thread.sleep(1000));
            assertThrows(BrokenBarrierException.class, () ->
                    barrier.await());
        });


        assertEquals(0, barrier.getNumberWaiting());

        Thread.sleep(1500);
        assertEquals(1, barrier.getNumberWaiting());
        barrier.reset();

        //join threads
        assertDoesNotThrow(() -> future1.get());
        assertDoesNotThrow(() -> future2.get());


        assertTrue(barrier.isBroken());
        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    void await_interrupted() {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(3);

        assertEquals(0, barrier.getNumberWaiting());


        Thread mainThread = Thread.currentThread();
        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<?> future1 = service.submit(() -> {
            assertDoesNotThrow(() -> Thread.sleep(1000));
            mainThread.interrupt();
        });

        Future<?> future2 = service.submit(() -> {
            assertDoesNotThrow(() -> Thread.sleep(2000));
            assertThrows(BrokenBarrierException.class, () ->
                    barrier.await());
        });

        assertThrows(InterruptedException.class, () ->
                barrier.await());


        //join threads
        assertDoesNotThrow(() -> future1.get());
        assertDoesNotThrow(() -> future2.get());

        assertTrue(barrier.isBroken());
        assertEquals(0, barrier.getNumberWaiting());
    }
}
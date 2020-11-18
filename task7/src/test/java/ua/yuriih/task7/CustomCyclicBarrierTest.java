package ua.yuriih.task7;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;

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

        Thread thread1 = new Thread(() -> {
            assertDoesNotThrow(() -> barrier.await());
        }, "thread2");

        thread1.start();

        assertDoesNotThrow(() -> Thread.sleep(1000));
        
        assertThrows(RuntimeException.class, () ->
                barrier.await(), "Error in barrier action");
        

        thread1.join();

        assertEquals(0, barrier.getNumberWaiting());
        assertFalse(barrier.isBroken());
    }

    @Test
    void await_notBroken() throws InterruptedException {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(3);

        assertEquals(0, barrier.getNumberWaiting());


        Thread thread1 = new Thread(() -> {
            assertDoesNotThrow(() -> Thread.sleep(2000));
            assertDoesNotThrow(() -> barrier.await());
        });
        Thread thread2 = new Thread(() -> {
            assertDoesNotThrow(() -> Thread.sleep(1000));
            assertDoesNotThrow(() -> barrier.await());
        });

        thread1.start();
        thread2.start();


        assertEquals(0, barrier.getNumberWaiting());

        Thread.sleep(1500);
        assertEquals(1, barrier.getNumberWaiting());

        Thread.sleep(1500);
        assertEquals(2, barrier.getNumberWaiting());


        assertDoesNotThrow(() ->
            assertEquals(0, barrier.await())
        );


        thread1.join();
        thread2.join();

        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    void await_reset() throws InterruptedException {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(3);

        assertEquals(0, barrier.getNumberWaiting());


        Thread thread1 = new Thread(() -> {
            assertDoesNotThrow(() -> Thread.sleep(2000));
            assertThrows(BrokenBarrierException.class, () ->
                    barrier.await());
        });
        Thread thread2 = new Thread(() -> {
            assertDoesNotThrow(() -> Thread.sleep(1000));
            assertThrows(BrokenBarrierException.class, () ->
                    barrier.await());
        });

        thread1.start();
        thread2.start();


        assertEquals(0, barrier.getNumberWaiting());

        Thread.sleep(1500);
        assertEquals(1, barrier.getNumberWaiting());
        barrier.reset();

        thread1.join();
        thread2.join();


        assertTrue(barrier.isBroken());
        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    void await_interrupted() throws InterruptedException {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(3);

        assertEquals(0, barrier.getNumberWaiting());


        Thread thread1 = new Thread(() -> {
            assertDoesNotThrow(() -> Thread.sleep(2000));
            assertThrows(BrokenBarrierException.class, () ->
                    barrier.await());
        });
        Thread thread2 = new Thread(() -> {
            assertDoesNotThrow(() -> Thread.sleep(1000));
            assertThrows(InterruptedException.class, () ->
                    barrier.await());
        });

        thread1.start();
        thread2.start();


        assertEquals(0, barrier.getNumberWaiting());

        Thread.sleep(1500);
        assertEquals(1, barrier.getNumberWaiting());
        thread2.interrupt();

        thread1.join();
        thread2.join();


        assertTrue(barrier.isBroken());
        assertEquals(0, barrier.getNumberWaiting());
    }
}
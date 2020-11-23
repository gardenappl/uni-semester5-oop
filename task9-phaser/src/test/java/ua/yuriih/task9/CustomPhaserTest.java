package ua.yuriih.task9;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class CustomPhaserTest {
    @Test
    public void arrive() {
        CustomPhaser phaser = new CustomPhaser();
        assertEquals(0, phaser.getPhase());

        assertEquals(0, phaser.bulkRegister(5));

        for (int i = 0; i < 5; i++) {
            assertEquals(i, phaser.getArrivedParties());
            assertEquals(5 - i, phaser.getUnarrivedParties());
            assertEquals(0, phaser.arrive());
        }

        assertEquals(1, phaser.getPhase());
        assertEquals(0, phaser.getArrivedParties());
        assertEquals(5, phaser.getUnarrivedParties());
        assertEquals(5, phaser.getRegisteredParties());
    }

    @Test
    public void arrive_await_nonBlocking() {
        CustomPhaser phaser = new CustomPhaser();

        assertEquals(0, phaser.bulkRegister(2));

        assertEquals(0, phaser.arrive());
        assertEquals(1, phaser.awaitAdvance(0));
    }

    @Test
    public void await_multiThreaded() throws InterruptedException{
        CustomPhaser phaser = new CustomPhaser();
        phaser.bulkRegister(3);

        //Await on main thread + 2 threads

        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<?> future1 = service.submit(() ->
                phaser.awaitAdvance(0)
        );
        Future<?> future2 = service.submit(() ->
                phaser.awaitAdvance(0)
        );


        Thread.sleep(500);
        assertEquals(2, phaser.getArrivedParties());
        assertEquals(1, phaser.getUnarrivedParties());

        assertEquals(1, phaser.awaitAdvance(0));

        //Join threads
        assertDoesNotThrow(() -> future1.get());
        assertDoesNotThrow(() -> future2.get());


        assertEquals(1, phaser.getPhase());
        assertEquals(0, phaser.getArrivedParties());
    }

    @Test
    public void await_multiThreaded_unregister() throws InterruptedException {
        CustomPhaser phaser = new CustomPhaser();
        phaser.bulkRegister(3);

        //Await on 2 threads, main thread will arrive and deregister

        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<?> future1 = service.submit(() ->
                phaser.awaitAdvance(0)
        );
        Future<?> future2 = service.submit(() ->
                phaser.awaitAdvance(0)
        );

        Thread.sleep(500);

        assertEquals(0, phaser.getPhase());
        assertEquals(2, phaser.getArrivedParties());
        assertEquals(1, phaser.getUnarrivedParties());

        assertEquals(0, phaser.arriveAndDeregister());
        assertEquals(1, phaser.getPhase());

        //Join threads
        assertDoesNotThrow(() -> future1.get());
        assertDoesNotThrow(() -> future2.get());


        assertEquals(1, phaser.getPhase());
        assertEquals(0, phaser.getArrivedParties());
        assertEquals(2, phaser.getRegisteredParties());

        //De-register until no more registered parties left

        assertEquals(1, phaser.arriveAndDeregister());
        assertEquals(1, phaser.arriveAndDeregister());
        assertThrows(IllegalStateException.class, () ->
                phaser.arriveAndDeregister());
    }
}
package ua.yuriih.task9;

import org.junit.jupiter.api.Test;

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
    }

    @Test
    public void arrive_await_nonBlocking() {
        CustomPhaser phaser = new CustomPhaser();

        assertEquals(0, phaser.bulkRegister(2));

        assertEquals(0, phaser.arrive());
        assertEquals(1, phaser.awaitAdvance(0));
    }

    @Test
    public void await_multiThreaded() {
        CustomPhaser phaser = new CustomPhaser();
        phaser.bulkRegister(3);

        Thread thread1 = new Thread(() ->
                phaser.awaitAdvance(0)
        );
        Thread thread2 = new Thread(() ->
                phaser.awaitAdvance(0)
        );

        thread1.start();
        thread2.start();


        assertDoesNotThrow(() -> Thread.sleep(500));
        assertEquals(2, phaser.getArrivedParties());
        assertEquals(1, phaser.getUnarrivedParties());

        assertEquals(1, phaser.awaitAdvance(0));


        assertDoesNotThrow(() -> thread1.join());
        assertDoesNotThrow(() -> thread2.join());


        assertEquals(1, phaser.getPhase());
        assertEquals(0, phaser.getArrivedParties());
    }

    @Test
    public void await_multiThreaded_unregister() {
        CustomPhaser phaser = new CustomPhaser();
        phaser.bulkRegister(3);

        Thread thread1 = new Thread(() ->
                phaser.awaitAdvance(0)
        );
        Thread thread2 = new Thread(() ->
                phaser.awaitAdvance(0)
        );

        thread1.start();
        thread2.start();


        assertDoesNotThrow(() -> Thread.sleep(500));
        assertEquals(0, phaser.getPhase());
        assertEquals(2, phaser.getArrivedParties());
        assertEquals(1, phaser.getUnarrivedParties());

        assertEquals(0, phaser.arriveAndDeregister());
        assertEquals(1, phaser.getPhase());

        assertDoesNotThrow(() -> thread1.join());
        assertDoesNotThrow(() -> thread2.join());


        assertEquals(1, phaser.getPhase());
        assertEquals(0, phaser.getArrivedParties());
        assertEquals(2, phaser.getRegisteredParties());

        assertEquals(1, phaser.arriveAndDeregister());
        assertEquals(1, phaser.arriveAndDeregister());
        assertThrows(IllegalStateException.class, () ->
                phaser.arriveAndDeregister());
    }
}
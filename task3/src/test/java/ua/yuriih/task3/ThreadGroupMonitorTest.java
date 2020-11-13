package ua.yuriih.task3;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class ThreadGroupMonitorTest {
    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void busyWait(int seconds) {
        long nanoTimeStart = System.nanoTime();
        while (System.nanoTime() - nanoTimeStart < seconds * 1_000_000_000L) {
            //busy wait
        }
    }
    
    @Test
    void run() {
        ThreadGroup groupRoot = new ThreadGroup("root");
        Thread threadRoot1 = new Thread(groupRoot, () -> sleep(3));
        threadRoot1.start();
        Thread threadRoot2 = new Thread(groupRoot, () -> busyWait(1));
        threadRoot2.start();
        Thread threadRoot3 = new Thread(groupRoot, () -> sleep(10000));
        threadRoot3.setDaemon(true);
        threadRoot3.start();

        ThreadGroup group1 = new ThreadGroup(groupRoot, "child1");
        Thread thread11 = new Thread(group1, () -> busyWait(3), "child1-1");
        thread11.start();
        Thread thread12 = new Thread(group1, () -> sleep(1), "child2-2");
        thread12.start();

        ThreadGroup group2 = new ThreadGroup(groupRoot, "child2");
        Thread thread21 = new Thread(group2, () -> busyWait(5));
        thread21.start();

        ThreadGroupMonitor monitor = new ThreadGroupMonitor(System.out, groupRoot);
        monitor.run();

        assertDoesNotThrow(() -> Thread.sleep(2000));
        monitor.run();
    }
}
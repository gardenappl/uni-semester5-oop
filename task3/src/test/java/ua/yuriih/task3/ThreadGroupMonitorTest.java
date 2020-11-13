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
    
    @Test
    void run() {
        ThreadGroup group = new ThreadGroup("root");
        Thread threadRoot1 = new Thread(group, () -> sleep(3), "1");
        threadRoot1.start();
        Thread threadRoot2 = new Thread(group, () -> sleep(1), "2");
        threadRoot2.start();
        Thread threadRoot3 = new Thread(group, () -> sleep(10000), "3");
        threadRoot3.start();

        ThreadGroup group1 = new ThreadGroup(group, "child1");
        Thread thread11 = new Thread(group, () -> sleep(3), "1");
        thread11.start();
        Thread thread21 = new Thread(group, () -> sleep(1), "2");
        thread21.start();

        ThreadGroupMonitor monitor = new ThreadGroupMonitor(System.out, group);
        monitor.run();

        assertDoesNotThrow(() -> Thread.sleep(2000));
        monitor.run();
    }
}
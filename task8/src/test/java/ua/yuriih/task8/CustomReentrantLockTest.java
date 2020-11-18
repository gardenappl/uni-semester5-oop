package ua.yuriih.task8;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomReentrantLockTest {
    @Test
    void test_tryLock() throws InterruptedException {
        CustomReentrantLock lock = new CustomReentrantLock();

        assertTrue(lock.tryLock());
        lock.unlock();

        Thread otherThread = new Thread(() -> {
            lock.lock();
            assertDoesNotThrow(() -> Thread.sleep(1000));
            lock.unlock();
        });

        otherThread.start();

        for (int i = 0; i < 9; i++) {
            Thread.sleep(100);
            assertFalse(lock.tryLock());
        }

        Thread.sleep(500);
        assertTrue(lock.tryLock());

        lock.unlock();
    }
}
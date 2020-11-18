package ua.yuriih.task8;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CustomReentrantLockTest {
    @Test
    void tryLock() throws InterruptedException {
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

    @Test
    void lock_reentrant() {
        CustomReentrantLock lock = new CustomReentrantLock();

        assertTrue(lock.tryLock());
        lock.lock();
        assertTrue(lock.tryLock());

        lock.unlock();
        lock.unlock();
        lock.unlock();
        assertThrows(IllegalMonitorStateException.class, () -> lock.unlock());
    }

    @Test
    void lock_interruptibly() throws InterruptedException {
        CustomReentrantLock lock = new CustomReentrantLock();

        lock.lock();

        Thread otherThread = new Thread(() -> {
            lock.lock();
            assertThrows(InterruptedException.class, () -> lock.lockInterruptibly());
            lock.unlock();
        });
        otherThread.start();

        Thread.sleep(100);
        otherThread.interrupt();

        lock.unlock();
    }

    @Test
    void tryLock_timeout() throws InterruptedException {
        CustomReentrantLock lock = new CustomReentrantLock();


        Thread otherThread = new Thread(() -> {
            lock.lock();
            assertDoesNotThrow(() -> Thread.sleep(1000));
            lock.unlock();
        });
        otherThread.start();

        Thread.sleep(100);
        
        assertFalse(lock.tryLock(100, TimeUnit.MILLISECONDS));

        otherThread.join();

        
        assertTrue(lock.tryLock(100, TimeUnit.MILLISECONDS));
        lock.unlock();
    }
}
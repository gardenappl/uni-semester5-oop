package ua.yuriih.task8;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CustomReentrantLockTest {
    @Test
    void tryLock() throws InterruptedException {
        CustomReentrantLock lock = new CustomReentrantLock();

        //Lock and unlock from single thread

        assertTrue(lock.tryLock());
        lock.unlock();

        //Lock from another thread

        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<?> future = service.submit(() -> {
            lock.lock();
            assertDoesNotThrow(() -> Thread.sleep(1000));
            lock.unlock();
        });

        for (int i = 0; i < 9; i++) {
            Thread.sleep(100);
            assertFalse(lock.tryLock());
        }

        assertDoesNotThrow(() -> future.get());

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
    void lock_interruptibly() {
        CustomReentrantLock lock = new CustomReentrantLock();

        ExecutorService service = Executors.newSingleThreadExecutor();

        //Interrupt main thread from another thread

        Thread mainThread = Thread.currentThread();

        Future<?> future = service.submit(() -> {
            lock.lock();

            assertDoesNotThrow(() -> Thread.sleep(1000));
            mainThread.interrupt();
            assertDoesNotThrow(() -> Thread.sleep(100));

            lock.unlock();
        });

        assertDoesNotThrow(() -> Thread.sleep(500));
        assertThrows(InterruptedException.class, () -> lock.lockInterruptibly());
        assertDoesNotThrow(() -> future.get());
    }

    @Test
    void tryLock_timeout() throws InterruptedException {
        CustomReentrantLock lock = new CustomReentrantLock();

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<?> future = service.submit(() -> {
            lock.lock();
            assertDoesNotThrow(() -> Thread.sleep(1000));
            lock.unlock();
        });

        Thread.sleep(100);
        
        assertFalse(lock.tryLock(100, TimeUnit.MILLISECONDS));

        assertDoesNotThrow(() -> future.get());
        
        assertTrue(lock.tryLock(100, TimeUnit.MILLISECONDS));
        lock.unlock();
    }
}
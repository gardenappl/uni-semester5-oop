package ua.yuriih.task8;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CustomReentrantLock implements Lock {
    private final Object lock = new Object();
    
    private int holdCount = 0;
    private static final long NOT_OWNED = -1;
    private long ownerThreadId = NOT_OWNED;
    
    @Override
    public void lock() {
        long currentId = Thread.currentThread().getId();
        if (ownerThreadId == currentId) {
            holdCount++;
            return;
        }
        
        while (ownerThreadId != NOT_OWNED) {
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException ignored) {}
        }
        ownerThreadId = currentId;
        holdCount++;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        if (ownerThreadId == NOT_OWNED || ownerThreadId == Thread.currentThread().getId()) {
            lock();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        holdCount--;
        
        if (holdCount == 0) {
            ownerThreadId = NOT_OWNED;
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    @Override
    public Condition newCondition() {
        throw new IllegalArgumentException("Conditions not implemented");
    }
}

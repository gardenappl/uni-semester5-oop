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
        try {
            doLock(true, 0);
        } catch (InterruptedException e) {
            throw new RuntimeException("This method should never throw an InterruptedException", e);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException();

        doLock(false, 0);
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
    public boolean tryLock(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return doLock(false, timeUnit.toNanos(timeout));
    }

    private boolean doLock(boolean ignoreInterrupts, long timeoutNanos) throws InterruptedException {
        long currentId = Thread.currentThread().getId();
        if (ownerThreadId == currentId) {
            holdCount++;
            return true;
        }

        long endNanos = 0;
        if (timeoutNanos != 0)
            endNanos = System.nanoTime() + timeoutNanos;

        while (ownerThreadId != NOT_OWNED) {
            long millis = 0;
            int nanos = 0;
            
            if (timeoutNanos != 0) {
                long startNanos = System.nanoTime();
                if (endNanos > startNanos) {
                    millis = (endNanos - startNanos) / 1_000_000L;
                    nanos = (int) ((endNanos - startNanos) % 1_000_000L);
                } else {
                    return false;
                }
            }

            try {
                synchronized (lock) {
                    lock.wait(millis, nanos);
                }
            } catch (InterruptedException e) {
                if (!ignoreInterrupts)
                    throw e;
            }
        }
        ownerThreadId = currentId;
        holdCount++;
        return true;
    }


    @Override
    public void unlock() {
        if (ownerThreadId != Thread.currentThread().getId())
            throw new IllegalMonitorStateException("Current thread does not hold this lock.");
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

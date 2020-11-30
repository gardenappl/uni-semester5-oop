package ua.yuriih.task7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;

public class CustomCyclicBarrier {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCyclicBarrier.class);
    
    private final Object lock;
    private final Runnable barrierAction;
    private final int parties;

    private int arrivalIndex;
    private int threadsToWakeUp;
    private boolean isBroken;
    
    public CustomCyclicBarrier(int parties) {
        this(parties, null);
    }
    
    public CustomCyclicBarrier(int parties, Runnable barrierAction) {
        if (parties <= 0)
            throw new IllegalArgumentException("CyclicBarrier works with 1 or more parties.");
        this.parties = parties;
        this.barrierAction = barrierAction;
        this.arrivalIndex = parties;
        this.threadsToWakeUp = parties;
        this.lock = new Object();
    }
    
    public int getNumberWaiting() {
        return parties - arrivalIndex;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void reset() {
        synchronized (this) {
            threadsToWakeUp = getNumberWaiting();
            isBroken = true;
            arrivalIndex = 0;
        }
        synchronized (lock) {
            lock.notifyAll();
        }
    }
    
    private synchronized void onWakeUp() {
        threadsToWakeUp--;

        //Last thread to wake up
        if (threadsToWakeUp == 0) {
            arrivalIndex = parties;
            threadsToWakeUp = parties;
        }
    }

    public int await() throws InterruptedException, BrokenBarrierException {
        if (isBroken)
            throw new BrokenBarrierException();
        
        int currentIndex;
        synchronized (this) {
            arrivalIndex--;
            currentIndex = arrivalIndex;
        }
        
        while (arrivalIndex > 0) {
            LOGGER.info("Arrival index: {}", arrivalIndex);
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                reset();
                onWakeUp();
                throw e;
            }
        }

        if (currentIndex == 0) {
            try {
                if (barrierAction != null)
                    barrierAction.run();
            } catch (Exception e) {
                isBroken = true;
                throw e;
            } finally {
                LOGGER.info("Waking up the others");
                threadsToWakeUp = getNumberWaiting();
                synchronized (lock) {
                    lock.notifyAll();
                }

                onWakeUp();
            }
        } else {
            onWakeUp();
        }

        if (isBroken)
            throw new BrokenBarrierException();
        else
            return currentIndex;
    }
}

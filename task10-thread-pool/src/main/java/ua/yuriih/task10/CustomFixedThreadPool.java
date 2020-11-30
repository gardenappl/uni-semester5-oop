package ua.yuriih.task10;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;

public class CustomFixedThreadPool implements Executor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomFixedThreadPool.class);

    private final Thread[] threads;
    private final ThreadPoolRunnable[] threadPoolRunnables;
    private final LinkedList<Runnable> runnableQueue;
    private volatile boolean isShuttingDown;

    public CustomFixedThreadPool(int threadCount) {
        if (threadCount <= 0)
            throw new IllegalArgumentException("Thread pool must have at least one thread.");

        threadPoolRunnables = new ThreadPoolRunnable[threadCount];
        for (int i = 0; i < threadPoolRunnables.length; i++)
            threadPoolRunnables[i] = new ThreadPoolRunnable(this, i);

        threads = new Thread[threadCount];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(threadPoolRunnables[i]);
            threads[i].start();
        }

        runnableQueue = new LinkedList<>();
    }

    public void shutdown() {
        isShuttingDown = true;
    }

    public List<Runnable> shutdownNow() {
        isShuttingDown = true;
        for (Thread thread : threads)
            thread.interrupt();

        return runnableQueue;
    }

    @Override
    public void execute(@NotNull Runnable runnable) {
        if (isShuttingDown)
            return;
        //Add it to the queue as soon as possible,
        //so it can be enqueued right away by onRunnableFreed from another thread.
        runnableQueue.add(runnable);
        tryStartNextRunnable(-1);
    }

    public int getQueueSize() {
        return runnableQueue.size();
    }

    private void onRunnableFreed(int id) {
        tryStartNextRunnable(id);
    }

    private synchronized void tryStartNextRunnable(int slot) {
        if (runnableQueue.isEmpty())
            return;

        if (slot != -1) {
            threadPoolRunnables[slot].setCurrentRunnable(runnableQueue.removeFirst());
        } else {
            for (ThreadPoolRunnable threadPoolRunnable : threadPoolRunnables) {
                if (!threadPoolRunnable.isRunning()) {
                    threadPoolRunnable.setCurrentRunnable(runnableQueue.removeFirst());
                    return;
                }
            }
        }
    }


    private static class ThreadPoolRunnable implements Runnable {
        private Runnable currentRunnable;
        private final int slot;
        private final CustomFixedThreadPool threadPool;

        public ThreadPoolRunnable(CustomFixedThreadPool threadPool, int slot) {
            this.slot = slot;
            this.threadPool = threadPool;
        }

        @Override
        public void run() {
            mainLoop:
            while (true) {
                while (currentRunnable == null) {
                    try {
                        synchronized (this) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        if (threadPool.isShuttingDown)
                            break mainLoop;
                    }
                }

                try {
                    currentRunnable.run();
                } catch (Exception e) {
                    LOGGER.error("Exception in thread pool", e);
                }

                currentRunnable = null;
                threadPool.onRunnableFreed(this.slot);
            }
        }

        public boolean isRunning() {
            return currentRunnable != null;
        }

        public void setCurrentRunnable(@NotNull Runnable currentRunnable) {
            if (isRunning())
                throw new IllegalStateException("ThreadPoolRunnable already has a Runnable");

            this.currentRunnable = currentRunnable;
            synchronized (this) {
                this.notify();
            }
        }
    }
}

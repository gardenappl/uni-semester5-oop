package ua.yuriih.task3;

import java.io.*;
import java.util.Arrays;

public class ThreadGroupMonitor implements Runnable {
    private final ThreadGroup threadGroup;
    private final PrintStream printStream;
    private final int millisInterval;

    public ThreadGroupMonitor(OutputStream outputStream, ThreadGroup threadGroup, int millisInterval) {
        this.threadGroup = threadGroup;
        if (outputStream instanceof PrintStream)
            this.printStream = (PrintStream)outputStream;
        else
            this.printStream = new PrintStream(outputStream);
        this.millisInterval = millisInterval;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            printTree(0, threadGroup);
            try {
                Thread.sleep(millisInterval);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }

    private void printWithIndent(int level, String s) {
        for (int i = 0; i < level; i++)
            printStream.print(" -  ");
        printStream.println(s);
    }
    
    private void printTree(int level, Thread thread) {
        printWithIndent(level, "---------");
        printWithIndent(level, "Thread: " + thread.getName());
        printWithIndent(level, "---------");
        if (thread.isDaemon())
            printWithIndent(level, "Daemon");
        printWithIndent(level, "ID: " + thread.getId());
        printWithIndent(level, "State: " + thread.getState());
        printWithIndent(level, "Priority: " + thread.getPriority());
    }
    
    private void printTree(int level, ThreadGroup group) {
        int estThreadCount = group.activeCount();
        int estGroupCount = group.activeGroupCount();
        printWithIndent(level, "---------");
        printWithIndent(level, "Group: " + group.getName());
        printWithIndent(level, "---------");
        if (group.isDaemon())
            printWithIndent(level, "Daemon");
        printWithIndent(level, "Destroyed: " + group.isDestroyed());
        printWithIndent(level, "Max. priority: " + group.getMaxPriority());


        Thread[] childThreads = new Thread[estThreadCount * 2 + 1];
        ThreadGroup[] childGroups = new ThreadGroup[estGroupCount * 2 + 1];
        
        group.enumerate(childThreads, false);
        group.enumerate(childGroups, false);


        boolean reachedEnd = false;
        for (Thread childThread : childThreads) {
            if (childThread == null) {
                reachedEnd = true;
                break;
            }
            printTree(level + 1, childThread);
        }
        if (!reachedEnd) {
            printWithIndent(level + 1, "... possibly more ...");
        }


        reachedEnd = false;
        for (ThreadGroup childGroup : childGroups) {
            if (childGroup == null) {
                reachedEnd = true;
                break;
            }
            printTree(level + 1, childGroup);
        }
        if (!reachedEnd) {
            printWithIndent(level + 1, "... possibly more ...");
        }
    }
}

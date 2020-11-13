package ua.yuriih.task3;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class ThreadGroupMonitor implements Runnable {
    ThreadGroup threadGroup;
    PrintStream printStream;

    public ThreadGroupMonitor(PrintStream printStream, ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
        this.printStream = printStream;
    }

    @Override
    public void run() {
        printTree(0, threadGroup);
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
        printWithIndent(level, "ID: " + thread.getId());
        printWithIndent(level, "State: " + thread.getState());
        printWithIndent(level, "Is daemon: " + thread.isDaemon());
    }
    
    private void printTree(int level, ThreadGroup group) {
        int estThreadCount = group.activeCount();
        int estGroupCount = group.activeGroupCount();
        printWithIndent(level, "---------");
        printWithIndent(level, "Group: " + group.getName());
        printWithIndent(level, "---------");
        printWithIndent(level, "Destroyed: " + group.isDestroyed());
        printWithIndent(level, "Is daemon: " + group.isDaemon());
        //printWithIndent(level, "Est. active threads: " + estThreadCount);
        //printWithIndent(level, "Est. active groups: " + estGroupCount);
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

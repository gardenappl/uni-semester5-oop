package ua.yuriih.task3;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ThreadGroupMonitorTest {
    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void busyWait(int seconds) {
        long nanoTimeStart = System.nanoTime();
        while (System.nanoTime() - nanoTimeStart < seconds * 1_000_000_000L) {
            //busy wait
        }
    }
    
    @Test
    void run() {
        ThreadGroup groupRoot = new ThreadGroup("root");
        Thread threadRoot1 = new Thread(groupRoot, () -> sleep(3), "root1");
        threadRoot1.start();
        Thread threadRoot2 = new Thread(groupRoot, () -> busyWait(1), "root2");
        threadRoot2.start();
        Thread threadRoot3 = new Thread(groupRoot, () -> sleep(10000), "root3");
        threadRoot3.setDaemon(true);
        threadRoot3.start();

        ThreadGroup group1 = new ThreadGroup(groupRoot, "child1");
        Thread thread11 = new Thread(group1, () -> busyWait(3), "child1-1");
        thread11.start();
        Thread thread12 = new Thread(group1, () -> sleep(1), "child2-2");
        thread12.start();

        ThreadGroup group2 = new ThreadGroup(groupRoot, "child2");
        Thread thread21 = new Thread(group2, () -> busyWait(5), "child2-1");
        thread21.start();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ThreadGroupMonitor monitor = new ThreadGroupMonitor(outputStream, groupRoot, 2000);
        Thread monitorThread = new Thread(monitor);


        monitorThread.start();
        
        assertDoesNotThrow(() -> Thread.sleep(500));


        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        assertLinesMatchStream(Arrays.asList(
                "---------",
                "Group: root",
                "---------",
                "Destroyed: false",
                "Max. priority: 10",
                " -  ---------",
                " -  Thread: root1",
                " -  ---------",
                " -  ID: 15",
                " -  State: TIMED_WAITING",
                " -  ---------",
                " -  Thread: root2",
                " -  ---------",
                " -  ID: 16",
                " -  State: RUNNABLE",
                " -  ---------",
                " -  Thread: root3",
                " -  ---------",
                " -  Daemon",
                " -  ID: 17",
                " -  State: TIMED_WAITING",
                " -  ---------",
                " -  Group: child1",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10",
                " -   -  ---------",
                " -   -  Thread: child1-1",
                " -   -  ---------",
                " -   -  ID: 18",
                " -   -  State: RUNNABLE",
                " -   -  ---------",
                " -   -  Thread: child2-2",
                " -   -  ---------",
                " -   -  ID: 19",
                " -   -  State: TIMED_WAITING",
                " -  ---------",
                " -  Group: child2",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10",
                " -   -  ---------",
                " -   -  Thread: child2-1",
                " -   -  ---------",
                " -   -  ID: 20",
                " -   -  State: RUNNABLE"
                ), inputStream);

        outputStream.reset();


        assertDoesNotThrow(() -> Thread.sleep(2000));
        monitorThread.interrupt();
        assertDoesNotThrow(() -> monitorThread.join());


        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        assertLinesMatchStream(Arrays.asList(
                "---------",
                "Group: root",
                "---------",
                "Destroyed: false",
                "Max. priority: 10",
                " -  ---------",
                " -  Thread: root1",
                " -  ---------",
                " -  ID: 15",
                " -  State: TIMED_WAITING",
                " -  ---------",
                " -  Thread: root3",
                " -  ---------",
                " -  Daemon",
                " -  ID: 17",
                " -  State: TIMED_WAITING",
                " -  ---------",
                " -  Group: child1",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10",
                " -   -  ---------",
                " -   -  Thread: child1-1",
                " -   -  ---------",
                " -   -  ID: 18",
                " -   -  State: RUNNABLE",
                " -  ---------",
                " -  Group: child2",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10",
                " -   -  ---------",
                " -   -  Thread: child2-1",
                " -   -  ---------",
                " -   -  ID: 20",
                " -   -  State: RUNNABLE"
        ), inputStream);
    }


    private void assertLinesMatchStream(List<String> expectedLines, InputStream stream) {
        Scanner scanner = new Scanner(stream);

        ArrayList<String> inputLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            inputLines.add(scanner.nextLine());
        }

        assertLinesMatch(expectedLines, inputLines);
    }
}
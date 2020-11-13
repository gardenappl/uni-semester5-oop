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
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ThreadGroupMonitorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGroupMonitorTest.class);
    
    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void busyWait(float seconds) {
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
        Thread thread21 = new Thread(group2, () -> busyWait(1), "child2-1");
        thread21.start();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ThreadGroupMonitor monitor = new ThreadGroupMonitor(outputStream, groupRoot, 2000);
        Thread monitorThread = new Thread(monitor);


        monitorThread.start();
        
        assertDoesNotThrow(() -> Thread.sleep(500));

        Function<String, String> replaceUnwanted = (String line) -> {
            if (line.contains("ID: "))
                return "(not testing IDs)";
            else
                return null;
        };

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
                "(not testing IDs)",
                " -  State: TIMED_WAITING",
                " -  Priority: 5",
                " -  ---------",
                " -  Thread: root2",
                " -  ---------",
                "(not testing IDs)",
                " -  State: RUNNABLE",
                " -  Priority: 5",
                " -  ---------",
                " -  Thread: root3",
                " -  ---------",
                " -  Daemon",
                "(not testing IDs)",
                " -  State: TIMED_WAITING",
                " -  Priority: 5",
                " -  ---------",
                " -  Group: child1",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10",
                " -   -  ---------",
                " -   -  Thread: child1-1",
                " -   -  ---------",
                "(not testing IDs)",
                " -   -  State: RUNNABLE",
                " -   -  Priority: 5",
                " -   -  ---------",
                " -   -  Thread: child2-2",
                " -   -  ---------",
                "(not testing IDs)",
                " -   -  State: TIMED_WAITING",
                " -   -  Priority: 5",
                " -  ---------",
                " -  Group: child2",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10",
                " -   -  ---------",
                " -   -  Thread: child2-1",
                " -   -  ---------",
                "(not testing IDs)",
                " -   -  State: RUNNABLE",
                " -   -  Priority: 5"
        ), inputStream, replaceUnwanted);

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
                "(not testing IDs)",
                " -  State: TIMED_WAITING",
                " -  Priority: 5",
                " -  ---------",
                " -  Thread: root3",
                " -  ---------",
                " -  Daemon",
                "(not testing IDs)",
                " -  State: TIMED_WAITING",
                " -  Priority: 5",
                " -  ---------",
                " -  Group: child1",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10",
                " -   -  ---------",
                " -   -  Thread: child1-1",
                " -   -  ---------",
                "(not testing IDs)",
                " -   -  State: RUNNABLE",
                " -   -  Priority: 5",
                " -  ---------",
                " -  Group: child2",
                " -  ---------",
                " -  Destroyed: false",
                " -  Max. priority: 10"
        ), inputStream, replaceUnwanted);
    }


    private void assertLinesMatchStream(List<String> expectedLines, 
                                        InputStream stream,
                                        Function<String, String> filter) {
        Scanner scanner = new Scanner(stream);

        ArrayList<String> inputLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String filteredLine = filter.apply(line);
            if (filteredLine != null)
                inputLines.add(filteredLine);
            else
                inputLines.add(line);
            LOGGER.info(line);
        }

        assertLinesMatch(expectedLines, inputLines);
    }
}
package ua.yuriih.lab1;

import ua.yuriih.lab1.view.TrainConsoleView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public static void testPrintStream(Consumer<PrintStream> printAction,
                                       int maxBytes,
                                       String... expectedLines) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(maxBytes);
        PrintStream printStream = new PrintStream(outputStream);

        printAction.accept(printStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Scanner scanner = new Scanner(inputStream);
        int i = 0;
        while (scanner.hasNextLine()) {
            assertEquals(expectedLines[i], scanner.nextLine());
            i++;
        }
    }
}

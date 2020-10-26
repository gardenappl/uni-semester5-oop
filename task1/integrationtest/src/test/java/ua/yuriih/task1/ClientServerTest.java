package ua.yuriih.task1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServerTest {
    @Test
    @DisplayName("Send and receive ExampleObject")
    public void sendAndReceive() throws InterruptedException {
        //Setup client and server
        AtomicReference<SerializationServer> server = new AtomicReference<>();
        assertDoesNotThrow(() ->
            server.set(new SerializationServer(0, new ExampleObject(1, "two", 3)))
        );

        Thread serverThread = new Thread(server.get());
        int portNumber = server.get().getListeningPort();

        SerializationClient client = new SerializationClient("localhost", portNumber);


        //Test
        serverThread.start();
        ExampleObject readObject = client.tryRead();

        assertEquals(1, readObject.getExampleInt());
        assertEquals("two", readObject.getExampleString());


        //Make sure server exits cleanly
        serverThread.join();
    }
}

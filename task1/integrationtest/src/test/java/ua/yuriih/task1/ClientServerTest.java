package ua.yuriih.task1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServerTest {
    @Test
    @DisplayName("Send and receive ExampleObject")
    public void sendAndReceive() throws InterruptedException {
        //Setup client and server
        AtomicReference<SerializationServer> server = new AtomicReference<>();
        AtomicReference<ServerSocket> serverSocket = new AtomicReference<>();
        assertDoesNotThrow(() -> {
            serverSocket.set(new ServerSocket(0));
            server.set(new SerializationServer(serverSocket.get(), new ExampleObject(1, "two", 3)));
        });

        Thread serverThread = new Thread(server.get());
        int portNumber = serverSocket.get().getLocalPort();

        AtomicReference<SerializationClient> client = new AtomicReference<>();
        assertDoesNotThrow(() ->
            client.set(new SerializationClient(new Socket("localhost", portNumber)))
        );


        //Test
        serverThread.start();
        ExampleObject readObject = client.get().tryRead();

        assertEquals(1, readObject.getExampleInt());
        assertEquals("two", readObject.getExampleString());


        //Make sure server exits cleanly
        serverThread.join();
    }
}

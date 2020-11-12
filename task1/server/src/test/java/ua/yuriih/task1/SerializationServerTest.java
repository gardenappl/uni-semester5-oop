package ua.yuriih.task1;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SerializationServerTest {
    @Test
    public void createNonNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new SerializationServer(new ServerSocket(0), null)
        );
    }

    @Test
    public void run() throws Exception {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(arrayOutputStream);

        ServerSocket serverSocket = mock(ServerSocket.class);
        when(serverSocket.accept()).thenReturn(socket);


        ExampleObject exampleObject = new ExampleObject(1, "Example2", 3);

        new SerializationServer(serverSocket, exampleObject).run();


        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);

        ExampleObject sentObject = (ExampleObject)objectInputStream.readObject();
        assertEquals(exampleObject.getExampleInt(), sentObject.getExampleInt());
        assertEquals(exampleObject.getExampleString(), sentObject.getExampleString());
        //otherInt doesn't need to be equal
    }
}

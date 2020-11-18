package ua.yuriih.task1;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SerializationClientTest {
    @Test
    void tryRead() {
        assertDoesNotThrow(() -> {
            ExampleObject exampleObject = new ExampleObject(1, "2", 3);

            ExampleObject receivedObject = tryRead_doTestObject(exampleObject);

            assertEquals(exampleObject.getExampleString(), receivedObject.getExampleString());
            assertEquals(exampleObject.getExampleInt(), receivedObject.getExampleInt());
            //otherInt doesn't need to be equal
        });
    }

    @Test
    void tryRead_wrongType() {
        String notExampleObject = "Not an ExampleObject instance, this should crash";

        assertThrows(ClassCastException.class, () -> 
                tryRead_doTestObject(notExampleObject)
        );
    }
    
    ExampleObject tryRead_doTestObject(Object exampleObject) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(exampleObject);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());


        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);


        return new SerializationClient(socket).tryRead();
    }
}
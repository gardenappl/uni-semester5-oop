package ua.yuriih.task1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationServerTest {
    @Test
    public void serializationServerCreateNonNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new SerializationServer(0, null)
        );
    }

    @Test
    public void serializationServerCreatePortTaken() {
        ExampleObject object = new ExampleObject(1, "1", 1);
        AtomicReference<SerializationServer> server1 = new AtomicReference<>();
        assertDoesNotThrow(() ->
                server1.set(new SerializationServer(0, object))
        );

        int portNumber = server1.get().getListeningPort();
        assertThrows(IOException.class, () ->
                new SerializationServer(portNumber, object));
    }
}

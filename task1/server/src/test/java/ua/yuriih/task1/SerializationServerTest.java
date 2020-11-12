package ua.yuriih.task1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationServerTest {
    @Test
    public void serializationServerCreateNonNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new SerializationServer(new ServerSocket(0), null)
        );
    }
}

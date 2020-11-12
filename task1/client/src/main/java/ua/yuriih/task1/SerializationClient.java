package ua.yuriih.task1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class SerializationClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationClient.class);
    
    private final Socket socket;
    
    public SerializationClient(Socket socket) {
        this.socket = socket;
    }
    
    public ExampleObject tryRead() {
        try (
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) { 
            return (ExampleObject) in.readObject();

        } catch (IOException e) {
            LOGGER.error("I/O error while getting input stream", e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Tried to serialize object with unknown class.", e);
            System.exit(1);
        }
        return null;
    }
}

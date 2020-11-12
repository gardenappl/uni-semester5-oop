package ua.yuriih.task1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.io.*;

public class SerializationServer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationServer.class);

    private final ExampleObject objectToSend;
    private final ServerSocket serverSocket;
    
    public SerializationServer(ServerSocket serverSocket, ExampleObject objToSend) throws IOException {
        if (objToSend == null)
            throw new IllegalArgumentException("Object must not be null.");

        this.serverSocket = serverSocket;
        this.objectToSend = objToSend;
    }

    @Override
    public void run() {
        try (
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out =
                        new ObjectOutputStream(clientSocket.getOutputStream());
        ) {
            out.writeObject(objectToSend);

        } catch (IOException e) {
            LOGGER.error("I/O error", e);
        }
    }
}
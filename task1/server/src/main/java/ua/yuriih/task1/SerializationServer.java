package ua.yuriih.task1;

import java.net.*;
import java.io.*;

public class SerializationServer implements Runnable {
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
            System.err.println("I/O error on server!");
            System.err.println(e.getMessage());
        }
    }
}
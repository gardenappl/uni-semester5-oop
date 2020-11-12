package ua.yuriih.task1;

import java.io.*;
import java.net.*;

public class SerializationClient {
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
            System.err.println("I/O error while connecting to " + socket.getInetAddress().getHostName());
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Tried to serialize object with unknown class.");
            System.exit(1);
        }
        return null;
    }
}

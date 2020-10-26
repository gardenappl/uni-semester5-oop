package ua.yuriih.task1;

import java.io.*;
import java.net.*;

public class SerializationClient {
    private final String hostName;
    private final int portNumber;
    
    public SerializationClient(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }
    
    public ExampleObject tryRead() {
        try (
                Socket socket = new Socket(hostName, portNumber);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) { 
            return (ExampleObject) in.readObject();

        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O error while connecting to " + hostName);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Tried to serialize object with unknown class.");
            System.exit(1);
        }
        return null;
    }
}

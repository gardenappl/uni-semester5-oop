package ua.yuriih.task1;

import java.io.*;
import java.net.*;

public class SerializationClient {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java ua.yuriih.task1.SerialzationClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket socket = new Socket(hostName, portNumber);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) { 
            ExampleObject obj = (ExampleObject) in.readObject();
            System.out.println(obj.toString());

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
    }
}

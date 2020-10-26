package ua.yuriih.task1;

import java.net.*;
import java.io.*;

public class SerializationServer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ua.yuriih.task1.SerializationServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out =
                        new ObjectOutputStream(clientSocket.getOutputStream());
        ) {

            ExampleObject object = new ExampleObject(4891, "Hello", 100000);
            out.writeObject(object);

        } catch (IOException e) {
            System.out.println("I/O error on server!");
            System.out.println(e.getMessage());
        }
    }
}
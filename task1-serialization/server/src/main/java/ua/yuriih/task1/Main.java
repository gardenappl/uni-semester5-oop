package ua.yuriih.task1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ua.yuriih.task1.SerializationServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        
        ExampleObject object = new ExampleObject(4891, "Hello", 100000);
        try {
            new SerializationServer(new ServerSocket(portNumber), object).run();
        } catch (Exception e) {
            LOGGER.error("Error!", e);
        }
    }
}

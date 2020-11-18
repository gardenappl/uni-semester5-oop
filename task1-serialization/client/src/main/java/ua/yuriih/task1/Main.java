package ua.yuriih.task1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java ua.yuriih.task1.SerialzationClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            SerializationClient client = new SerializationClient(new Socket(hostName, portNumber));
            System.out.println(client.tryRead().toString());
        } catch (IOException e) {
            LOGGER.error("I/O error", e);
        }
    }
}

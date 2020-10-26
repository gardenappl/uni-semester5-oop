package ua.yuriih.task1;

public class Main {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java ua.yuriih.task1.SerialzationClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        SerializationClient client = new SerializationClient(hostName, portNumber);
        System.out.println(client.tryRead().toString());
    }
}

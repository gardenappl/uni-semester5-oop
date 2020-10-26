package ua.yuriih.task1;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ua.yuriih.task1.SerializationServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        
        ExampleObject object = new ExampleObject(4891, "Hello", 100000);
        try {
            new SerializationServer(portNumber, object).run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

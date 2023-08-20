import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client", extraArgs)) {
            Demo.PrinterPrx twoway = Demo.PrinterPrx.checkedCast(
                    communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);

            if (twoway == null) {
                throw new Error("Invalid proxy");
            }

            Scanner scanner = new Scanner(System.in);

            String username = System.getProperty("user.name");
            String hostname = getHostname();

            while (true) {
                System.out.print("Enter a message (or 'exit' to quit): ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                String formattedMessage = username + ":" + hostname + ":" + message;
                String response = twoway.printString(formattedMessage);
                System.out.println("Server Response: " + response);
            }
        }
    }

    private static String getHostname() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException ex) {
            return "Unknown";
        }
    }
}

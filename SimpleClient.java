import java.io.*;
import java.net.*;

public class SimpleClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 1234;

        try (Socket socket = new Socket(hostname, port)) {

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Use a separate thread to handle server-side input
            Thread clientInputThread = new Thread(() -> {
                String serverResponse;
                try {
                    while ((serverResponse = reader.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (SocketException e) {
                    System.out.println("Server has closed the connection.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientInputThread.start();

            // Main thread to handle client-side input
            String message;

            while (true) {
                System.out.print("Enter message: ");
                message = consoleReader.readLine();
                writer.println(message);

                // Exit loop if "exit" is typed
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Process is Close");

                    break;
                }
            }

            socket.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

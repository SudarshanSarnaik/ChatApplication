import java.io.*;
import java.net.*;

public class SimpleServer {
    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            // Get input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Use a separate thread to handle server-side input
            Thread serverInputThread = new Thread(() -> {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                String serverMessage;

                try {
                    while ((serverMessage = consoleReader.readLine()) != null) {
                        // Send message to client
                        writer.println("Server: " + serverMessage);

                        // If server types "exit", close connection and terminate
                        if (serverMessage.equalsIgnoreCase("exit")) {
                            socket.close();
                            System.out.println("Server is shutting down.");
                            System.exit(0); // Terminate the server
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverInputThread.start();

            // Main thread to handle client-side input
            String clientMessage;

            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from client: " + clientMessage);

                // Exit loop if client sends "exit"
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client has disconnected.");
                    break;
                }
            }

            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

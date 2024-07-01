import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 4444;

        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("Connected to server on port " + SERVER_PORT);

            // Thread to handle incoming server messages
            Thread serverMessageHandler = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = reader.readLine()) != null) {
                        System.out.println("Server response: " + serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverMessageHandler.start();

            // Handle user input
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                writer.println(userInputLine); // Send user input to server
            }

            serverMessageHandler.join(); // Wait for the server handler thread to finish

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

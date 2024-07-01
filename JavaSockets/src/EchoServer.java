import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class EchoServer {
    private static final int PORT = 4444;
    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>(); // Thread-safe list for client handlers

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for a client connection
                System.out.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler); // Add client handler to the list
                new Thread(clientHandler).start(); // Start a new thread to handle client communication
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send message to all connected clients
    public static void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Runnable class to handle client communication
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);

                    // Example: respond to specific commands
                    if (inputLine.equalsIgnoreCase("quit")) {
                        break;
                    } else if (inputLine.equalsIgnoreCase("broadcast")) {
                        broadcastMessage("Broadcast message from server!");
                    } else {
                        writer.println("Server echo: " + inputLine); // Echo back to client
                    }
                }

                System.out.println("Client disconnected: " + clientSocket);
                clients.remove(this); // Remove client handler from the list

                reader.close();
                writer.close();
                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Send a message to this client
        public void sendMessage(String message) {
            writer.println(message);
        }
    }
}

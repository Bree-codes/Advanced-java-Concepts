import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        final int PORT = 4444; // Port number for the server to listen on

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for a client connection
                System.out.println("New client connected: " + clientSocket);

                // Create a new thread to handle client communication
                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Runnable class to handle client communication
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    writer.println("Server echo: " + inputLine); // Echo back to client
                }

                System.out.println("Client disconnected: " + clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
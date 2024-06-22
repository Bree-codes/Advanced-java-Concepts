import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                writer.println(userInputLine); // Send user input to server
                System.out.println("Server response: " + reader.readLine()); // Receive server's echo
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

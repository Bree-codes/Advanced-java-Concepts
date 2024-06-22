import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

    public static void main(String[] args) {
        final int PORT = 5555; // Port number for server to bind

        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("Server is running and listening on port " + PORT);

            while (true) {
                byte[] receiveData = new byte[1024];

                // Create a DatagramPacket to receive data from the client
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket); // Receive packet from client

                // Extract client's IP address and port
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Process received data
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client [" + clientAddress.getHostAddress() + ":" + clientPort + "]: " + receivedMessage);

                // Prepare data to send back to the client
                String serverResponse = "Hello from Server..";
                byte[] sendData = serverResponse.getBytes();

                // Create a DatagramPacket to send data to the client
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket); // Send packet to client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


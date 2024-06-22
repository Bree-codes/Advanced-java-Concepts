import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost"; // Server's IP address or hostname
        final int SERVER_PORT = 5555; // Server's port number

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);

            // Message to send to the server
            String message = "Hello from Client..";
            byte[] sendData = message.getBytes();

            // Create a DatagramPacket to send data to the server
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            clientSocket.send(sendPacket); // Send packet to server

            // Prepare to receive response from the server
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Receive packet from server
            clientSocket.receive(receivePacket);

            // Process received data
            String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server response: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

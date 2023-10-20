import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * UDP Client class containing implementations specific to UDP protocol.
 */
public class UDPClient extends AbstractClientFunctionClass{

  // To read the content of the file and check if any malformed content is present.
  private static String udpClientRead(String line){
    String currentTimeMillis = getCurrentTime();
    if(isValidOp(line)) {
      String timestampedMessage = currentTimeMillis + " " +"Sent to server: "+ line;
      System.out.println(timestampedMessage);
      return line;
    }
    else {
      String timestampedMessage = currentTimeMillis + " " + "received malformed request of length:" +
              line.length();
      System.out.println(timestampedMessage);
      return "received malformed request of length:" + line.length();
    }
  }
  public static void main(String[] args) {
    // Server IP or hostname
    String serverAddress = args[0];
    int serverPort = Integer.parseInt(args[1]); // Server port
    int timeout = 100000;
    try (DatagramSocket clientSocket = new DatagramSocket();
         BufferedReader fileReader = new BufferedReader(new FileReader("commands.txt"))) {
      String line;
      
      while ((line = fileReader.readLine()) != null) {
        // Send data to the server
        String clientMessage = udpClientRead(line);
        clientMessage+=" "+timeout;
        //Encoding the data to be sent to the server.
        sendingUDPrequest(serverAddress, serverPort, clientSocket, clientMessage);
        // Receive data from the server
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        // Decoding the data gotten from the server.
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
        System.out.println(getCurrentTime()+" Received from server: " + receivedMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void sendingUDPrequest(String serverAddress, int serverPort, DatagramSocket clientSocket, String clientMessage) throws IOException {
    byte[] sendData = clientMessage.getBytes(StandardCharsets.UTF_8);
    InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);
    clientSocket.send(sendPacket);
  }
}

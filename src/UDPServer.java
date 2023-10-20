import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * UDP server class implementing operations specific to UDP protocol.
 */
public class UDPServer extends AbstractServerFunctionClass{
  public static void main(String[] args) {
    // Choose a port for the server.
    int serverPort = Integer.parseInt(args[1]);
    // DatagramSocket object created to start the server on the specified port.
    try (DatagramSocket serverSocket = new DatagramSocket(serverPort)) {
      UDPServer obj = new UDPServer();
      RMIFunctions stub = (RMIFunctions) UnicastRemoteObject.exportObject(obj,0);
      // Bind the remote object's stub in the registry
      Registry registry = LocateRegistry.getRegistry();
      registry.bind("RMIFunctions", stub);
      System.out.println(getCurrentTime()+" Server is listening on port " + serverPort);
      // Server functions.
      while (true) {
        long startTime = System.currentTimeMillis();
        //Thread.sleep(10);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        // Receive data from the client.
        serverSocket.receive(receivePacket);
        // To store the client address and port.
        String addressClient = receivePacket.getAddress().getHostAddress();
        String portClient = String.valueOf(receivePacket.getPort());
        // To decode the message received from the client
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
        int timeout = Integer.parseInt(receivedMessage.split(" ")[receivedMessage.split(" ").length-1]);
        receivedMessage = receivedMessage.substring(0,receivedMessage.length()-String.valueOf(timeout).length()-1);
        String currentTimeMillis = getCurrentTime();
        System.out.println(currentTimeMillis+" Received from Client: "+receivedMessage+
                " from "+addressClient+":"+portClient);
        //To get the server response for the respective operation.
        String toClient = "";
        toClient = obj.redirectingRequests(receivedMessage, toClient, addressClient, portClient);
        long endTime = System.currentTimeMillis();
        if(endTime - startTime > timeout){
          toClient = getCurrentTime()+ " Request Timed out with request taking: "+
                  (endTime-startTime)+" ms to process!";
        }
        // Echo the received data back to the client.
        sendingUDPresponse(serverSocket, receivePacket, toClient);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
//    catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
  }

  private static void sendingUDPresponse(DatagramSocket serverSocket, DatagramPacket receivePacket, String toClient) throws IOException {
    InetAddress clientAddress = receivePacket.getAddress();
    int clientPort = receivePacket.getPort();
    // Encoding the data to be sent to the client.
    byte[] sendData = toClient.getBytes(StandardCharsets.UTF_8);
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
    serverSocket.send(sendPacket);
  }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * TCP server class containing implementations specific to TCP protocol.
 */
public class TCPServer extends AbstractServerFunctionClass{

  public static void main(String[] args) {
    // Port number is taken from the terminal argument.
    int portNumber = Integer.parseInt(args[1]);
    // Starting the server.
    try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
      System.out.println(getCurrentTime() + " Server is listening on port " + portNumber);
      // To keep the server running always.
      while (true) {
        try {
          TCPServer obj = new TCPServer();
          RMIFunctions stub = (RMIFunctions) UnicastRemoteObject.exportObject(obj,0);
          // Bind the remote object's stub in the registry
          Registry registry = LocateRegistry.getRegistry();
          registry.bind("RMIFunctions", stub);
          Socket clientSocket = serverSocket.accept();
          long startTime = System.currentTimeMillis();

          //Thread.sleep(10);
          // To read the file content sent from the client.
          try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
               PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String clientMessage = in.readLine();
            if (clientMessage != null) {
              // Setting the timeout of the TCP server.
              int timeout = Integer.parseInt(clientMessage.split(" ")[clientMessage.split(" ").length-1]);
              clientMessage = clientMessage.substring(0,clientMessage.length()-String.valueOf(timeout).length()-1);
              String serverResponse = "";
              String clientAddress = clientSocket.getInetAddress().toString();
              String clientPort = String.valueOf(clientSocket.getPort());
              // Traversing every line of the client message.
              System.out.println(getCurrentTime() + " Received from Client: " + clientMessage +
                      " from " + clientAddress + ":" + clientPort);
              serverResponse = obj.redirectingRequests(clientMessage, serverResponse, clientAddress, clientPort);
              // Write the server response back to the client.
              long endTime = System.currentTimeMillis();
              // Checking Timeout condition.
              if(endTime - startTime > timeout){
                serverResponse = getCurrentTime()+ " Request Timed out with request taking: "+
                        (endTime-startTime)+" ms to process!";
              }
              out.println(serverResponse);
            }
          }
        }
        catch (Exception e){
          e.printStackTrace();
        }
//        catch (InterruptedException e) {
//          throw new RuntimeException(e);
//        }
      }
    }catch (IOException e) {
          e.printStackTrace();
        }

    }

}

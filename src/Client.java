import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class Client extends AbstractClientFunctionClass{
  public static void main(String[] args){
    try {
      String serverAddress = args[0];
      int clientPort = Integer.parseInt(args[1]);
      Registry registry = LocateRegistry.getRegistry(serverAddress, clientPort);
      RMIServer stub = (RMIServer) registry.lookup("RMIServer");
      System.out.println(getCurrentTime()+" Client is running");
      System.out.println(getCurrentTime()+" This is a menu driven program with the following commands: PUT/GET/DELETE");
      System.out.println(getCurrentTime()+" Enter 'exit' to exit");
      Scanner sc = new Scanner(System.in);
      while (true) {
        String clientMessage = "";
        clientMessage+=sc.nextLine();
        if (clientMessage.equalsIgnoreCase("exit")) {
          System.out.println(getCurrentTime()+" Client disconnected");
          break;
        }
        String toServer = clientRead(clientMessage);
        String serverResponse = stub.redirectingRequests(toServer,"",serverAddress,String.valueOf(clientPort));
        System.out.println(getCurrentTime()+" Received from server: "+serverResponse);
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
}

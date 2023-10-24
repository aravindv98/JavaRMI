import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
      System.out.println(getCurrentTime()+" This is a menu driven program with the following commands: PUT/GET/DELETE/file");
      System.out.println(getCurrentTime()+" Enter 'exit' to exit");
      Scanner sc = new Scanner(System.in);
      while (true) {
        String clientMessage = "";
        clientMessage += sc.nextLine();
        switch (clientMessage.toUpperCase()) {
          case "FILE": {
            File file = new File("commands.txt");
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
              String line;
              while ((line = reader.readLine()) != null) {
                String toServer = clientRead(line);
                String serverResponse = stub.redirectingRequests(toServer, "", serverAddress, String.valueOf(clientPort));
                System.out.println(getCurrentTime() + " Received from server: " + serverResponse);
              }
              break;
            }
          }
          case "EXIT" :{
            System.out.println(getCurrentTime() + " Client disconnected");
            break;
          } default: {
            String toServer = clientRead(clientMessage);
            String serverResponse = stub.redirectingRequests(toServer, "", serverAddress, String.valueOf(clientPort));
            System.out.println(getCurrentTime() + " Received from server: " + serverResponse);
          }
        }
        if (clientMessage.equalsIgnoreCase("exit")) {
          break;
        }
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
}

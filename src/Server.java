import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * An RMI Server to implement the remote RMI interface and extend the abstract
 * server implementation class.
 */
public class Server extends AbstractServerFunctionClass{
  public static void main(String[] args) {
    // Port number is taken from the terminal argument.
    int portNumber = Integer.parseInt(args[0]);
    Server obj = new Server();
    // Starting the server.
    try  {
      // Creation of a remote object for the client to access.
      RMIServer skeleton = (RMIServer) UnicastRemoteObject.exportObject(obj,0);
      Registry registry = LocateRegistry.createRegistry(portNumber);
      registry.rebind("RMIServer", skeleton);
      System.out.println(getCurrentTime() + " Server is listening on port " + portNumber);
    }catch (Exception e) {
      e.printStackTrace();
    }

  }
}

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An RMI interface containing the methods to be implemented by the server and to be exposed to the
 * client through a remote object.
 */
public interface RMIServer extends Remote{
  void putCommand(String key, String value) throws RemoteException;
  String getCommand(String key) throws RemoteException;
  boolean deleteCommand(String key) throws RemoteException;
  String deleteOperation(String key, String clientAddress, String clientPort) throws RemoteException;
  String getOperation( String key, String clientAddress, String clientPort) throws RemoteException;
  String putOperation(String key, String clientAddress, String clientPort) throws RemoteException;
  String performOperation(String clientMessage, String serverResponse, String clientAddress, String clientPort) throws RemoteException;
  String checkTimeOut(long startTime, long endTime) throws RemoteException;
}

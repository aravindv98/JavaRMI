import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RMIServer extends Remote{
  void putCommand(String key, String value) throws RemoteException;
  String getCommand(String key) throws RemoteException;
  boolean deleteCommand(String key) throws RemoteException;
  String deleteOperation(String key, String clientAddress, String clientPort) throws RemoteException;
  String getOperation( String key, String clientAddress, String clientPort) throws RemoteException;
  String putOperation(String key, String clientAddress, String clientPort) throws RemoteException;
  String redirectingRequests(String clientMessage, String serverResponse, String clientAddress, String clientPort) throws RemoteException;
  String checkTimeOut(long startTime, long endTime) throws RemoteException;
}

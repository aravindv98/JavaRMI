import java.rmi.server.RemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract server class that contains the implementation of the hashmap functions.
 */
public abstract class AbstractServerFunctionClass extends RemoteObject implements RMIServer{
  // Map to store, get and delete key/value pairs.
  private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
  // Pre-populating the key,value pairs of the hashmap.
  public AbstractServerFunctionClass(){
    map.put("1","Arsenal");
    map.put("2","City");
    map.put("3","Liverpool");
    map.put("4","Chelsea");
    map.put("5","United");
  }
  //Implementing the put operation of the Map. (PUT)
  public synchronized void putCommand(String key, String value){
    map.put(key,value);
  }
  //Implementing the get operation of the Map. (GET)
  public synchronized String getCommand(String key){
    return map.get(key);
  }
  //Implementing the delete operation of the Map. (DELETE)
  public synchronized boolean deleteCommand(String key){
    if(map.containsKey(key)){
      map.remove(key);
      return true;
    }
    return false;
  }
  // Returns the current system time of the server.
  public synchronized static String getCurrentTime(){
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    return currentDateTime.format(formatter);
  }
  // Helper function to invoke the delete operation of the map and to check if the delete operation
  // can be performed using the arguments provided.
  public synchronized String deleteOperation(String key, String clientAddress, String clientPort) {
    //To check the number of arguments passed for deletion.
    long startTime = System.currentTimeMillis();
    // Thread.sleep(500);
    String [] validChunks = key.split(" ");
    // If valid, then send confirmation message to client.
    if(deleteCommand(key)&&validChunks.length==1) {
      long endTime = System.currentTimeMillis();
        String responseMessage = checkTimeOut(startTime, endTime);
        if (responseMessage.equals("")) {
          System.out.println(getCurrentTime() + " Sent to client:" + " DELETE operation with key: " + key + " completed"
                  + " from " + clientAddress + ":" + clientPort);
          return "DELETE operation with key: " + key + " completed" + " from " + clientAddress + ":" + clientPort;
        } else {
          return responseMessage;
        }
    }
    else {
      System.out.println(getCurrentTime() + " Sent to client:" + " Invalid DELETE operation received from client"
              + " from " + clientAddress + ":" + clientPort);
      return "Invalid DELETE operation received from client" + " from " + clientAddress + ":" + clientPort;
    }
  }
  // Helper function to invoke the get operation of the map and to check if the get operation
  // can be performed using the arguments provided.
  public synchronized String getOperation( String key, String clientAddress, String clientPort) {
    long startTime = System.currentTimeMillis();
    // Thread.sleep(500);
    //To check the number of arguments passed for get.
    String [] validChunks = key.split(" ");
    // If valid, then send confirmation message to client.
    if(getCommand(key)!=null&&validChunks.length==1) {
      String value = getCommand(key);
      long endTime = System.currentTimeMillis();
        String timeOutMessage = checkTimeOut(startTime, endTime);
        if (timeOutMessage.equals("")) {
          System.out.println(getCurrentTime() + " Sent to client:" + " GET operation with key: " + key + " gives value: " + value
                  + " from " + clientAddress + ":" + clientPort);
          return "GET operation with key: " + key + " gives value: " + value + " from " + clientAddress + ":" + clientPort;
        } else {
          return timeOutMessage;
        }
    }
    else {
      System.out.println(getCurrentTime() + " Sent to client:" + " Invalid GET operation received from client"
              + " from " + clientAddress + ":" + clientPort);
      return "Invalid GET operation received from client" + " from " + clientAddress + ":" + clientPort;
    }
  }
  // Helper function to invoke the put operation of the map and to check if the put operation
  // can be performed using the arguments provided.
  public synchronized String putOperation(String key, String clientAddress, String clientPort) {
    long startTime = System.currentTimeMillis();
    // Thread.sleep(500);
    //To check the number of arguments passed for put.
    String [] validChunks = key.split(" ");
    String realKey = key.split(" ", 2)[0];
    String value = key.split(" ", 2)[1];
    // If valid, then send confirmation message to client.
    if (!realKey.equals("") && !value.equals("") && validChunks.length==2) {
      putCommand(realKey, value);
      long endTime = System.currentTimeMillis();
      String timeOutMessage = checkTimeOut(startTime, endTime);
      if (timeOutMessage.equals("")) {
        System.out.println(getCurrentTime() + " Sent to client:" + " PUT operation with key: " + realKey + " and value: " + value + " completed"
                + " from " + clientAddress + ":" + clientPort);
        return "PUT operation with key: " + realKey + " and value: " + value + " completed"
                + " from " + clientAddress + ":" + clientPort;
      }
      else {
        return timeOutMessage;
      }
    }
    // Else, client to be informed of the erroneous arguments.
    else {
      System.out.println(getCurrentTime()+" Sent to client:"+" Invalid PUT operation received from client"
              +" from "+clientAddress+":"+clientPort);
      return "Invalid PUT operation received from client"
              +" from "+clientAddress+":"+clientPort;
    }


  }
  public synchronized String checkTimeOut(long startTime, long endTime){
    if(endTime - startTime > 10){
      return getCurrentTime()+ " Request Timed out with request taking: "+
              (endTime-startTime)+" ms to process!";
    }
    return "";
  }
  // Function to redirect the requests from the client to the respective methods.
  // The client calls only this method and the server handles all the remaining operations.
  public synchronized String performOperation(String clientMessage, String serverResponse, String clientAddress, String clientPort) {
    String operation = clientMessage.split(" ", 2)[0];
    String key = clientMessage.split(" ", 2)[1];
    switch (operation) {
      case "PUT": {
        serverResponse += putOperation(key, clientAddress, clientPort);
        break;
      }
      case "GET": {
        serverResponse += getOperation(key, clientAddress, clientPort);
        break;
      }
      case "DELETE": {
        serverResponse += deleteOperation(key, clientAddress, clientPort);
        break;
      }
      default: {
        serverResponse = clientMessage;
        System.out.println(getCurrentTime() + " Sent to client: "
                + serverResponse + " from " + clientAddress + ":" + clientPort);
      }
    }
    return serverResponse;
  }
}

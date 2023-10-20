import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract server class that contains the common implementations of the TCP and UDP servers.
 */
public abstract class AbstractServerFunctionClass implements RMIFunctions{
  // Map to store, get and delete key/value pairs.
  private static Map<String, String> map = new HashMap<>();
  //Implementing the put operation of the Map. (PUT)
  public void putCommand(String key, String value){
    map.put(key,value);
  }
  //Implementing the get operation of the Map. (GET)
  public String getCommand(String key){
    return map.get(key);
  }
  //Implementing the delete operation of the Map. (DELETE)
  public boolean deleteCommand(String key){
    if(map.containsKey(key)){
      map.remove(key);
      return true;
    }
    return false;
  }
  // Returns the current system time of the server.
  public static String getCurrentTime(){
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    return currentDateTime.format(formatter);
  }
  // Helper function to invoke the delete operation of the map and to check if the delete operation
  // can be performed using the arguments provided.
  public String deleteOperation(String key, String clientAddress, String clientPort) {
    //To check the number of arguments passed for deletion.
    String [] validChunks = key.split(" ");
    // If valid, then send confirmation message to client.
    if(deleteCommand(key)&&validChunks.length==1){
      System.out.println(getCurrentTime() +" Sent to client:"+ " DELETE operation with key: " + key +" completed"
              +" from "+clientAddress+":"+clientPort);
      return "DELETE operation with key: " + key +" completed"+" from "+clientAddress+":"+clientPort;
    }
    // Else, client to be informed of the erroneous arguments.
    else{
      System.out.println(getCurrentTime()+" Sent to client:"+" Invalid DELETE operation received from client"
              +" from "+clientAddress+":"+clientPort);
      return "Invalid DELETE operation received from client"+" from "+clientAddress+":"+clientPort;
    }
  }
  // Helper function to invoke the get operation of the map and to check if the get operation
  // can be performed using the arguments provided.
  public String getOperation( String key, String clientAddress, String clientPort) {
    //To check the number of arguments passed for get.
    String [] validChunks = key.split(" ");
    // If valid, then send confirmation message to client.
    if(getCommand(key)!=null&&validChunks.length==1){
      String value = getCommand(key);
      System.out.println(getCurrentTime() + " Sent to client:"+ " GET operation with key: " + key +" gives value: "+ value
              +" from "+clientAddress+":"+clientPort);
      return "GET operation with key: " + key +" gives value: "+ value+" from "+clientAddress+":"+clientPort;
    }
    // Else, client to be informed of the erroneous arguments.
    else{
      System.out.println(getCurrentTime()+" Sent to client:"+" Invalid GET operation received from client"
              +" from "+clientAddress+":"+clientPort);
      return "Invalid GET operation received from client"+" from "+clientAddress+":"+clientPort;
    }
  }
  // Helper function to invoke the put operation of the map and to check if the put operation
  // can be performed using the arguments provided.
  public String putOperation(String key, String clientAddress, String clientPort) {
    //To check the number of arguments passed for put.
    String [] validChunks = key.split(" ");
    String realKey = key.split(" ", 2)[0];
    String value = key.split(" ", 2)[1];
    // If valid, then send confirmation message to client.
    if (!realKey.equals("") && !value.equals("") && validChunks.length==2) {
      putCommand(realKey,value);
      System.out.println(getCurrentTime()+" Sent to client:"+" PUT operation with key: " + realKey +" and value: "+ value +" completed"
              +" from "+clientAddress+":"+clientPort);
      return "PUT operation with key: " + realKey +" and value: "+ value +" completed"
              +" from "+clientAddress+":"+clientPort;
    }
    // Else, client to be informed of the erroneous arguments.
    else {
      System.out.println(getCurrentTime()+" Sent to client:"+" Invalid PUT operation received from client"
              +" from "+clientAddress+":"+clientPort);
      return "Invalid PUT operation received from client"
              +" from "+clientAddress+":"+clientPort;
    }
  }
  public String redirectingRequests(String clientMessage, String serverResponse, String clientAddress, String clientPort) {
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

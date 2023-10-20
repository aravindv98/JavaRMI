import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract class containing the common implementations of both TCP and UDP clients.
 */
public abstract class AbstractClientFunctionClass {
  // To check if the operation is valid or not from client side.
  protected static boolean isValidOp(String content){
    String key = content.split(" ",2)[0];
    return key.equals("PUT")||key.equals("GET")||key.equals("DELETE");
  }
  // To return the current system time of the client.
  protected static String getCurrentTime(){
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    return currentDateTime.format(formatter);
  }
}

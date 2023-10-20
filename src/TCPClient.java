import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * TCP Client class containing the implementations specific to TCP protocol.
 */
public class TCPClient extends AbstractClientFunctionClass {
  // To read the client data from the file and check if any malformed content is present.
  private static String clientRead(String line) {
    if(isValidOp(line)) {
      String timestampedMessage = getCurrentTime() + " " +"Sent to server: "+ line;
      System.out.println(timestampedMessage);
      return line;
    }
    else {
      String timestampedMessage = getCurrentTime() + " " + "received malformed request of length:" +
              line.length();
      System.out.println(timestampedMessage);
      return "received malformed request of length:" + line.length();
    }
  }
  public static void main(String[] args) {
    // Server Address and port.
    String serverAddress = args[0];
    int serverPort = Integer.parseInt(args[1]);
    int timeoutMillis = 100000;
    // Initialising the TCP connection and i/p, o/p writing objects to exchange information
    // between server and client.
    File file = new File("commands.txt");
    try (FileInputStream fileInputStream = new FileInputStream(file);
         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
         BufferedReader reader = new BufferedReader(inputStreamReader)) {
      String line;
      Socket socket = new Socket();
      // Read the file and its contents.
      while ((line = reader.readLine()) != null) {
        socket = new Socket();
        socket.connect(new InetSocketAddress(serverAddress, serverPort), timeoutMillis);
        try(
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){
          // To check if any malformed content is present in the file.
          BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          String toServer="";
          toServer+=clientRead(line);
          toServer+=" "+timeoutMillis;
          out.println(toServer);
          String serverResponse = in.readLine();
          System.out.println(getCurrentTime()+" Received from server: "+serverResponse);
        }
       catch (IOException e) {
        e.printStackTrace();
      }
      socket.close();
    } socket.close();
      System.out.println(getCurrentTime()+" Client disconnected.");
  }
    catch (IOException e){
      e.printStackTrace();
    }
}
  }

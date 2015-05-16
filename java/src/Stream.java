import java.net.*;
import java.io.*;

public class Stream {
  public static void main(String[] args) {
    final String IP_ADDRESS = "128.54.178.135";
    final int LISTENING_PORT = 8080;
    InetAddress address = null;
    try {
      address = InetAddress.getByName(IP_ADDRESS);
    } catch(IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    StreamConsole console = new StreamConsole();
    StreamServer server = new StreamServer(LISTENING_PORT, address, console);
    //StreamServer server = new StreamServer(console);
    server.start();
  }
}

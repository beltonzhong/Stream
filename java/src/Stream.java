import java.net.*;
import java.io.*;

public class Stream {
  public static void main(String[] args) {
    final int LISTENING_PORT = 8080;
    String ipAddress = null;
    InetAddress address = null;
    try {
      ipAddress = InetAddress.getLocalHost().getHostAddress();
      address = InetAddress.getByName(ipAddress);
    } catch(IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    StreamConsole console = new StreamConsole();
    StreamServer server = new StreamServer(LISTENING_PORT, address, console);
    server.start();
  }
}

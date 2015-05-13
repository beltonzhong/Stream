import java.lang.*;
import java.net.*;
import java.io.*;

public class StreamServer extends Thread { 

  private static final int BACKLOG_LENGTH = 1;
  private ServerSocket servSock;
  private StreamConsole loggingConsole;

  public StreamServer(int port, InetAddress ipAddress, StreamConsole console) {
    loggingConsole = console;
    try {
      servSock = new ServerSocket(port, BACKLOG_LENGTH, ipAddress);
      loggingConsole.log("Created a new server at " + ipAddress + ":" + port);
    } catch(IOException e) {
      e.printStackTrace();
      loggingConsole.log("Failed to create a new server at " + ipAddress +
                         ":" + port);
      System.exit(1);
    }
  }
  
  @Override
  public void run() {

  }
}


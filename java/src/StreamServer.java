import java.lang.*;
import java.net.*;
import java.io.*;

public class StreamServer extends Thread { 

  private static final int BACKLOG_LENGTH = 1;
  private ServerSocket servSock;
  private StreamConsole loggingConsole;

  public StreamServer(int port, InetAddress ipAddress, StreamConsole console) {
    loggingConsole = console;
    loggingConsole.log("Attempting to create a server at " + ipAddress.getHostName() + "...");
    try {
      servSock = new ServerSocket(port, BACKLOG_LENGTH, ipAddress);
      loggingConsole.log("Created a new server at " + ipAddress.getHostName() + ":" + port + ".");
      loggingConsole.log();
    } catch(IOException e) {
      loggingConsole.log("Failed to create a new server at " + ipAddress +
                         ":" + port + ". Exiting.");
      System.exit(1);
    }
  }

  public StreamServer(StreamConsole console) {
    loggingConsole = console;
    loggingConsole.log("Attempting to create a server...");
    try {
      servSock = new ServerSocket();
      loggingConsole.log("Created a new server.");
      loggingConsole.log();
    } catch(IOException e) {
      e.printStackTrace();
      loggingConsole.log("Failed to create a new server. Exiting.");
      System.exit(1);
    }
    try {
      loggingConsole.log("Attempting to bind server to 127.0.0.1...");
      servSock.bind(new InetSocketAddress("127.0.0.1", 8080), 1);
      loggingConsole.log("Server bound to 127.0.0.1:8080.");
      loggingConsole.log();
    } catch(IOException e) {
      loggingConsole.log("Failed to bind server to 127.0.0.1. Exiting.");
      System.exit(1);
    }
  }
  
  @Override
  public void run() {
    Socket connectionSocket = null;
    InetAddress clientAddress = null;

    // Open the server and accept a connection
    loggingConsole.log("Waiting for connection...");
    while(connectionSocket == null) {
      try {
        connectionSocket = servSock.accept();
        clientAddress = connectionSocket.getInetAddress();
        loggingConsole.log("Connection accepted from: " + clientAddress.getHostAddress() + ".");
      } catch (IOException e) {
        loggingConsole.log("Connection failed. Error message:");
        loggingConsole.log(e.getMessage() + ".");
      }
    }
    
    // After accepting a connection, close the server
    while(!servSock.isClosed()) {
      loggingConsole.log();
      loggingConsole.log("Server closing...");
      try {	
        servSock.close();
      } catch(IOException e) {	  
        loggingConsole.log("Server close failed. Retrying...");
	loggingConsole.log();
      }
    }
    loggingConsole.log("Server closed.");
    loggingConsole.log();
  }
}


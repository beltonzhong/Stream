import com.sun.jna.*;

import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.*;

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
    Socket connectionSocket;
    InetAddress clientAddress = null;

    while(true) {
      // Open the server and accept a connection
      loggingConsole.log("Waiting for connection...");
      connectionSocket = null;
      while(connectionSocket == null) {
        try {
          connectionSocket = servSock.accept();
          clientAddress = connectionSocket.getInetAddress();
          loggingConsole.log("Connection accepted from: " + clientAddress.getHostAddress() + ".");
          loggingConsole.log();
        } catch (IOException e) {
          loggingConsole.log("Connection failed. Error message:");
          loggingConsole.log(e.getMessage() + ".");
        }
      }
      
      //Handle the connection request
      this.handleConnection(connectionSocket);
    }  
    /*
    // Close the server
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
    */
  }

  private void handleConnection(Socket connectionSocket) {
    // Open the input and output streams
    BufferedReader input = null;
    DataOutputStream output = null;

    loggingConsole.log("Attempting to open input stream...");
    try {
      input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      loggingConsole.log("Successfully opened input stream.");
      loggingConsole.log();
    } catch (IOException e) {
      loggingConsole.log("Failed to open input stream. Exiting.");
      return;
    }

    loggingConsole.log("Attempting to open output stream...");
    try {
      output = new DataOutputStream(connectionSocket.getOutputStream());
      loggingConsole.log("Successfully opened output stream.");
      loggingConsole.log();
    } catch (IOException e) {
      loggingConsole.log("Failed to open output stream. Exiting.");
      return;
    }

    // Read the request
    String[] request = null;

    loggingConsole.log("Reading request...");
    try {
      request = input.readLine().split(" ");
      loggingConsole.log("Read request.");
      loggingConsole.log();
    } catch(IOException e) {
      loggingConsole.log("Failed to read request. Exiting.");
      return;
    }

    // Parse the request type
    String reqType = null;

    loggingConsole.log("Attempting to parse request type...");
    reqType = request[0].toUpperCase();
    loggingConsole.log("Successfully parsed the request type.");
    loggingConsole.log();

    // Handle the request
    switch(reqType) {
      case "GET":
        loggingConsole.log("GET request received from " + connectionSocket.getInetAddress()
	                   .getHostAddress() + ".");
        this.handleGetRequest(input, output, request);
        break;
      default:
        loggingConsole.log("Received unsupported request type received from " + 
                           connectionSocket.getInetAddress().getHostAddress() + ".");
        loggingConsole.log("Sending 501...");
        try {
          output.writeBytes(constructHTTPHeader(501, ""));
          loggingConsole.log("Sent 501.");
          loggingConsole.log();
        } catch(IOException e) {
          loggingConsole.log("Failed to send 501.");
          loggingConsole.log();
        }
        return;
    }
  }

  private void handleGetRequest(BufferedReader input, DataOutputStream output, String[] request) {
    // Get the requested file name
    String fileName = null;
    String fileType = null;

    fileName = request[1];
    
    // Get the requested file
    FileInputStream fileStream = null;
    
    // Special case of sending the options
    if(fileName.equals("/options")) {
      this.sendOptionsList(output);
      return;
    }

    // Send index.html when no page is requested
    if(fileName.equals("/"))
      fileName += "html/index.html";
    fileType = fileName.substring(fileName.indexOf('.') + 1);

    loggingConsole.log("Attempting to open " + fileName + "...");
    try {
      fileStream = new FileInputStream("../.." + fileName);
      loggingConsole.log("Successfully opened " + fileName + ".");
    } catch(FileNotFoundException e) {
      loggingConsole.log("Failed to open " + fileName + ". Exiting.");
      loggingConsole.log("Sending 404...");
      try {
        output.writeBytes(constructHTTPHeader(404, ""));
        loggingConsole.log("Sent 404.");
      } catch(IOException ex) {
        loggingConsole.log("Failed to send 404.");
        loggingConsole.log();
      }
      return;

    }
    
    // Send the header
    loggingConsole.log("Sending 200...");
    try {
      output.writeBytes(constructHTTPHeader(200, fileType));
      loggingConsole.log("Sent 200.");
    } catch(IOException e) {
      loggingConsole.log("Failed to send 200.");
      loggingConsole.log();
    }

    // Send the file
    loggingConsole.log("Attempting to send " + fileName + "...");
    try {
      while(fileStream.available() > 0) {
        output.write(fileStream.read());
      }
      loggingConsole.log("Successfully sent " + fileName + ".");
      loggingConsole.log();
    } catch(IOException e) {
      loggingConsole.log("Failed to send " + fileName + ".");
      loggingConsole.log();
    }

    // Close the streams
    loggingConsole.log("Attempting to close streams...");
    try {
      if(fileStream != null) {
        fileStream.close();
        loggingConsole.log("Closed file stream.");
      }
      output.close();
      loggingConsole.log("Closed output stream.");
      loggingConsole.log();
    } catch(IOException e) {
      loggingConsole.log("Failed to close all streams.");
      loggingConsole.log();
    }
  }

  private String constructHTTPHeader(int responseCode, String fileType) {
    String header = "HTTP/1.0 ";
    switch(responseCode) {
      case 200:
        header += "200 OK";
        break;
      case 400:
        header += "400 Bad Request";
        break;
      case 403:
        header += "403 Forbidden";
        break;
      case 404:
        header += "404 Not Found";
        break;
      case 500:
        header += "500 Internal Server Error";
        break;
      case 501:
        header += "501 Not Implemented";
        break;
    }
    header += "\r\nConnection: keep-alive\r\nServer: Stream\r\n";
    switch(fileType) {
      case "html":
        header += "Content-Type: text/html\r\n";
        break;
      case "css":
        header += "Content-Type: text/css\r\n";
        break;
      case "ico":
        header += "Content-Type: image/x-icon\r\n";
        break;
      default:
        break;
    }
    header += "\r\n";
    return header;
  }

  private void sendOptionsList(DataOutputStream output) {
    loggingConsole.log("Received request for options.");

    // Send the header
    loggingConsole.log("Sending 200...");
    try {
      output.writeBytes(constructHTTPHeader(200, "json"));
      loggingConsole.log("Sent 200.");
    } catch(IOException e) {
      loggingConsole.log("Failed to send 200.");
      loggingConsole.log();
    }

    // Send the options list
    loggingConsole.log("Attempting to send options list...");
    try {
      output.writeBytes(this.constructOptionsListJSON());
      loggingConsole.log("Successfully sent options list.");
      loggingConsole.log();
    } catch(IOException e) {
      loggingConsole.log("Failed to send options list.");
      loggingConsole.log();
    }

    // Close the streams
    loggingConsole.log("Attempting to close output stream (no input stream " +
                       "was created)...");
    try {
      output.close();
      loggingConsole.log("Closed output stream.");
      loggingConsole.log();
    } catch(IOException e) {
      loggingConsole.log("Failed to close output stream.");
      loggingConsole.log();
    }
  }

  private String constructOptionsListJSON() {
    String json = "{\n\t\"names\":\n\t[\n";
    String line = null;
    try {
    } catch(IOException e) {
    }
    return json;
  }
}


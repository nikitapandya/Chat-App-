/*Nikita Pandya U40405881
 * Prof. Matta 
 * CS 455 - User
 * October 6, 2015
 */ 

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;
import java.net.UnknownHostException; 

public class User extends Thread {
  
  // The user socket
  private static Socket userSocket = null;
  // The output stream
  private static PrintStream output_stream = null;
  // The input stream
  private static BufferedReader input_stream = null;

  private static BufferedReader inputLine = null;
  private static boolean closed = false;
  //thread!
  private static String tName; 
  
  public static void main(String[] args) {
    
    // The default port.
    int portNumber = 58999;
    // The default host.
    String host = "localhost";

   if (args.length < 2) {
            System.out.println("Usage: java User <host> <portNumber>\n"
             + "Now using host=" + host + ", portNumber=" + portNumber);
  } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();  
          } 

    //Open a socket on a given host and port. Open (initialize) input and output streams.
    try {
      userSocket = new Socket(host, portNumber);
      output_stream = new PrintStream(userSocket.getOutputStream(), true);
      input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));  
      inputLine = new BufferedReader(new InputStreamReader(System.in)); 
              
    }
    
    catch (UnknownHostException e) {
      System.err.println("Don't know about host " + host); } 
    catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to the host " + host); }   
      
          /*
          * If everything has been initialized then create a listening thread to 
          * read from the server. 
          * Also send any user’s message to server until user logs out.
          */ 
      if(userSocket!= null && output_stream != null && input_stream != null) {
        try {
          //create new thread "user" 
          new Thread(new User()).start();
          while(!closed){
            String msg = inputLine.readLine().trim();
            if (msg.equals("LogOut")) {
              break;
               }
            output_stream.println(msg);  
            }

            //close connections 
            output_stream.close();
            input_stream.close();
            userSocket.close();
          }
          catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host " + host); }

          } // if 

  } //End main
  
 public void run() {
  /*
  * Keep on reading from the socket till we receive “### Bye …” from the
  * server. Once we received that then we want to break and close the connection.
  */
  String responseLine; 
  try {
    while ((responseLine = input_stream.readLine()) != null) {
      System.out.println("Server: " + responseLine); 
    }    
      closed = true; 
    }
    catch (IOException e) {
      System.out.println("Thread " + tName + " interrupted."); }

 } //End run 
  
} ///End Class User 
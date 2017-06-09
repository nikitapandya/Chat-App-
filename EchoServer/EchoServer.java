/*Nikita Pandya U40405881
 * Prof. Matta 
 * CS 455 - EchoServer 
 */ 

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;

/*
 * An echo server that simply echoes messages back.
 */
public class EchoServer {
    // Create a socket for the server 
    private static ServerSocket serverSocket = null;
    // Create a socket for the user 
    private static Socket userSocket = null;
    // The input stream
    private static BufferedReader input_stream = null;
    private static PrintStream output_stream = null;

    public static void main(String args[]) {
        
        // The default port number.
        int portNumber = 58999;
        if (args.length < 1) {
            System.out.println("Usage: java Server <portNumber>\n"
                                   + "Now using port number=" + portNumber + "\n");
            System.out.println("Welcome to Echo"); 
        } 
        else {
            portNumber = Integer.valueOf(args[0]).intValue();
            System.out.println("Welcome to Echo"); 
        }

        //Open a server socket on the portNumber (default 8000). 
        
        try {
          serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e) {
                System.out.println(e);
            }

        //Create a user socket for accepted connection 
        while (true) {
            try {
              
              userSocket = serverSocket.accept();
              PrintStream output_stream = new PrintStream(userSocket.getOutputStream(), true);
              BufferedReader input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
              
              //Reads message inputed from the user and echos back to the user 
              String inputLine; 
              inputLine = input_stream.readLine();
                System.out.println ("Server: " + inputLine); 
                output_stream.println(inputLine); 
            
             // Close the output stream, close the input stream, close the socket.
             input_stream.close();
             output_stream.close();
             userSocket.close(); 
             break; }
            
            catch (IOException e) {
                System.out.println(e);
            }
        } 
        
        try {
         serverSocket.close(); 
        }
        catch (IOException e) {
                System.out.println(e);}
    }
}





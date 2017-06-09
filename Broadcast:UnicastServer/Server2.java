/*Nikita Pandya U40405881
 * Prof. Matta 
 * CS 455 - User
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
 * A chat server that delivers public and private messages.
 */
public class Server2 {

  // Create a socket for the server  
  private static ServerSocket serverSocket = null;
  // Create a socket for the user
  private static Socket userSocket = null; 
  // Maximum number of users (5) 
  private static int maxUsersCount = 5; 
  // An array of threads for users
  private static userThread[] threads = new userThread[maxUsersCount]; 

    public static void main(String args[]) {

        // The default port number
        int portNumber = 58999; 
        
        if (args.length < 1) {
            System.out.println("Usage: java Server <portNumber>\n"+ "Now using port number=" + portNumber + "\n");
            System.out.println("Welcome to Server");  } 
        else {
            portNumber = Integer.valueOf(args[0]).intValue();
            System.out.println("Welcome to Nikita's Server"); }

        //create a server socket on the defauly portNumber  
        try {
            serverSocket = new ServerSocket(portNumber); }
        catch (IOException e) {
            System.out.println(e); }

        /*
         * Create a user socket for each connection and pass it to a new user
         * thread.
         */
        while (true) {
            try {
                userSocket = serverSocket.accept(); 
                //keeps track of how many threads are running in the server 
                int tCount; 
                //if space in thread array create a new user thread 
                for (tCount =0; tCount<maxUsersCount; tCount++){
                    if (threads[tCount] == null){
                        (threads[tCount] = new userThread(userSocket, threads)).start(); 
                        break; }
                    }
                //if the max number of users is reached close connections and send "server busy"
                if (maxUsersCount == tCount) {
                    PrintStream output_stream = new PrintStream(userSocket.getOutputStream());
                    output_stream.println("Server busy");
                    output_stream.close();
                    userSocket.close(); }

         } catch (IOException e) {
        System.out.println(e); }
        }  
    } //end Main
}//end Class Server

//Threads
class userThread extends Thread {
    
    private String userName = null;
    private BufferedReader input_stream = null;
    private PrintStream output_stream = null;
    private Socket userSocket = null;
    private final userThread[] threads;
    private int maxUsersCount;

    //constructor 
    public userThread(Socket userSocket, userThread[] threads) {
        this.userSocket = userSocket;
        this.threads = threads;
        maxUsersCount = threads.length;
    }
    
    public void run() {
      int maxUsersCount = this.maxUsersCount;
      userThread[] threads = this.threads;
        // Create input and output streams for this client, and start conversation.
        try{
        input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream())); 
        output_stream = new PrintStream(userSocket.getOutputStream()); 

        output_stream.println("Enter you name."); 
         userName = input_stream.readLine(); 
        output_stream.println("Welcome " + userName + " to our chat room ! ! !"); 
        output_stream.println("To leave enter LogOut in a new line.");

        //when new user that isnt this user thread enters 
        synchronized(this){
        for (int i = 0; i < maxUsersCount; i++) {
            if (threads[i] != null && threads[i] != this)
                threads[i].output_stream.println("*** A new user " + userName + " entered the chat room !!! ***");
        }}

        //if LogOut or max users not reached
        while (true) {
            String responseLine = input_stream.readLine();
        //When a user wants to leave 
        if(responseLine.equals("LogOut")) {
          //Says bye to the user leaving 
          output_stream.println("### Bye<" + userName + "> ###");
          //Informs other users connected to the server that this user is leaving 
          synchronized(this){
            for (int i = 0; i < maxUsersCount; i++) {
              if (threads[i] != null && threads[i] != this)
                threads[i].output_stream.println("*** The user " +userName+ " is leaving the chat room !!! ***");
            } } //sync & for 

          //after this user logs out set thread in the array to null so another user can use it Aka cleaning up 
          synchronized(this){
            for (int i = 0; i < maxUsersCount; i++) {
              if (threads[i] == this) 
                threads[i] = null; 
            } } //sync & for 
          break; 
        }//if LogOut condition

        //if you want to send a private or unicast message to another user 
        if (responseLine.startsWith("@")) {
            String[] message = responseLine.split(" "); 
            String user = message[0].replace("@", ""); 
            synchronized(this){
            for (int i =0; i<maxUsersCount; i++){
                if (threads[i] != null && threads[i] != this && threads[i].userName.equals(user)){
                    threads[i].output_stream.println("<" + userName + "> " + responseLine);
                    this.output_stream.println("<" + userName + "> " + responseLine);
                    break;
                }//if
            }}//for
        }//if

        else {
            synchronized(this){
                for (int i = 0; i < maxUsersCount; i++) {
                    if (threads[i] != null) 
                        threads[i].output_stream.println("<" + userName + "> " + responseLine); 
            } } //sync & for 
        }//else  
        }//While 

        // Close the output stream, close the input stream, close the socket.
        input_stream.close();
        output_stream.close();
        userSocket.close(); 
        }catch (IOException e) {
            System.out.println(e);}
    }//end run
}//end CLASS userThread 
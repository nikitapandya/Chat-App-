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
public class Server {
    private static ServerSocket serverSocket = null; // Create a socket for the server  
    private static Socket userSocket = null; // Create a socket for the user
    private static int maxUsersCount = 5; // Maximum number of users 
    private static userThread[] threads = new userThread[maxUsersCount]; // An array of threads for users

    public static void main(String args[]) {
        int portNumber = 58999; // The default port number.
        
        if (args.length < 1) {
            System.out.println("Usage: java Server <portNumber>\n"+ "Now using port number=" + portNumber + "\n");
            System.out.println("Welcome to Server");  } 
        else {
            portNumber = Integer.valueOf(args[0]).intValue();
            System.out.println("Welcome to Nikita's Server"); }

        // Create a client socket for each connection and pass it to a new client thread
        try {
            serverSocket = new ServerSocket(portNumber); }
        catch (IOException e) {
            System.out.println(e); }

        while (true) {
            try {
                userSocket = serverSocket.accept(); 
                int tCount; //keeps track of how many threads are running in the server 
                for (tCount =0; tCount<maxUsersCount; tCount++){
                    if (threads[tCount] == null){
                        (threads[tCount] = new userThread(userSocket, threads)).start(); 
                        break; }
                    }

                if (maxUsersCount == tCount) {
                    PrintStream output_stream = new PrintStream(userSocket.getOutputStream());
                    output_stream.println("Server busy");
                    output_stream.close();
                    userSocket.close(); }

         } catch (IOException e) {
        System.out.println(e); }
        } //While 
    } //Main
}//Class Server

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
        // Create input and output streams for this client, and start conversation.
        try{
        input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream())); 
        output_stream = new PrintStream(userSocket.getOutputStream()); 

        output_stream.println("Enter you name."); 
        String userName = input_stream.readLine(); 
        output_stream.println("Welcome " + userName + " to our chat room ! ! !"); 
        output_stream.println("To leave enter LogOut in a new line.");

        //when new user that isnt this user thread enters 
        synchronized(this){
        for (int i = 0; i < maxUsersCount; i++) {
            if (threads[i] != null && threads[i] != this)
                threads[i].output_stream.println("*** A new user " + userName + " entered the chat room !!! ***");
        }}

        //if LogOut or max users not reached
        synchronized(this) {
        while (true) {
            String responseLine = input_stream.readLine();
            if(responseLine.equals("LogOut")) {
                output_stream.println("*** Bye " +userName +" ***");
                break; }

            for (int i = 0; i < maxUsersCount; i++) {
                if (threads[i] != null) {
                    threads[i].output_stream.println("<" + userName + "> " + responseLine); }
            }
        }//While
        }
        
        //when user is leaving 
        synchronized(this){
        for (int i = 0; i < maxUsersCount; i++) {
            if (threads[i] != null && threads[i] != this)
                threads[i].output_stream.println("*** The user " +userName+ " is leaving the chat room !!! ***");
        }}

        synchronized(this){
        for (int i = 0; i < maxUsersCount; i++) {
            if (threads[i] == this) 
             threads[i] = null; 
        }}

        input_stream.close();
        output_stream.close();
        userSocket.close(); 
        }catch (IOException e) {
            System.out.println(e);}
    }//run
}//CLASS
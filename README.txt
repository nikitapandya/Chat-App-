PART 1: ECHO SERVER

In this part we have two files, EchoUser.java and EchoServer.java. 
The server is essentially an echo server, which simply echoes the
message it receives from the client. 

    EchoServer: 
    The server accepts, a port number that it will run
    at as a command line argument. After opening the socket,
    the server will create a user socket and accept the connection. 
    The server essentially repeatedly accepts an input message from
    a user and sends “echoes” back the same message.

    EchoUser:
    The user accepts, a host name and a port number for the
    server as command line arguments. This is how is creates a 
    TCP connection with the server (already running on the same 
    port number). The user essentially sends a message to the server. 
    When the user receives the message back “echoed message” from the
    server, it prints it and exits.

PART 2: BROADCAST SERVER

In this part we have two files, User.java and Server.java. 
In this part we implement multi-threading, which allows the 
server to communicate with multiple clients at the same time. 
Here we just broadcast the message to all the users connected 
with the server. 

    Server: 
    The server starts by creating a server socket on the port 
    number. Next when a user decides to connect to the server, 
    it creates a user socket for each connection and passes it
    to a new user thread. In the main function we also keep track
    of how many threads are running in the server, making sure the 
    we do not exceed the maxUsersCount. 
    The subclass userThread which extends the Threads class 
    handles all individual user threads and creates a private
    array of user threads. Additionally, an instance of userThread
    had access to all other threads that the server has access to.
    This is how the server broadcasts messages from one user to all
    other users connected. In the run function the server asks user
    for their name and broadcasts to other users connected (if any)
    that another user has joined. Essentially, when a user sends a
    message (sends to a thread in UserThread), the server sends
    “broadcasts” this message to all other users. When a user 
    sends “LogOut” we close the connection and remove the thread 
    from the array so other users can connect. 

    User: 
    This class extends the thread class. Fist we create a server
    socket at the server’s port number. Next in the while loop
    the user starts a listening thread before accepting broadcast
    messages. While the listening thread is running, it simply prints
    what it reads in. When the server signals that the connection is 
    closed on its end, the user listening also closes the connection.


PART 3: BROADCAST AND UNICAST SERVER


In this part we have one file, Server2.java which is an 
extension of Server.java from part 2. This server is 
different because in addition to broadcasting the 
message to all the users connected to the server, 
we add a unicast capability. Meaning users can send
“private” messages to specific users connected to the
server. 

***Note name of file is Server2.java***

    Server2: 
    We simply extended server.java from part 2 
    In the run() function in the userThread subclass 
    We added a check when a user is joining the chat-room.
    If the input line began with ‘@‘, the server sends the 
    message to only the specified user. It does this by 
    iterating through the thread array to find the corresponding 
    user and then output the message to only that user.
    Additionally, the private message is also echoed to the
    user who sent it so as to let her know that the message
    was sent. This statement is also under the synchronized 
    header because it keeps all concurrent threads in execution
    to be in sync. 

Possible improvements: 
— For the broadcast and unicast server, should make sure all 
  userNames are unique.  
- For the unicast server: fix a way to send private messages if 
  the user has two names 
- For the unicast server: when we want to send a private message
  to a non-existent user, it should warn the user
- An extra enter required after “LogOut” (even though connection is already closed) 




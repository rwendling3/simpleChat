// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  ChatIF clientUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  //Handle #login from client
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	
	String[] message = msg.toString().split(" ");
	if(message[0].equals("#login") && message.length > 1) {
		this.sendToAllClients("SERVER MSG> A new client is attempting to connect to the server.");
		if(client.getInfo("loginID") == null) {
			client.setInfo("loginID", message[1]);
			
			String loginID = client.getInfo("loginID").toString();
			this.sendToAllClients("SERVER MSG> Message received: " + msg + " from " + loginID);
			this.sendToAllClients("SERVER MSG> " + loginID + " has logged on");
			
		}else {
			System.out.println("ERROR: Client already has login ID");
			try{
				client.close();
			}catch (IOException e) {
				System.out.println("ERROR: closing socket");
				quit();
			}
		}	
	}else {
		if(client.getInfo("loginID").equals(null)) {
			System.out.println("Input loginID using #login <loginid>");	
		}else {
			String loginID = client.getInfo("loginID").toString();
			
			System.out.println("SERVER MSG> Message received: " + msg + " from " + loginID);
			
		    this.sendToAllClients(loginID + "> " + msg);
		}
	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
 
 
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client: " + client  + "connected");
  }
  
  @Override
  synchronized protected void clientDisconnected(
		    ConnectionToClient client) {
	  System.out.println("Client: " + client  + " disconnected"); 
	  sendToAllClients("Client: " + client  + " disconnected");
  }
  public void handleMessageFromClientUI(String message) throws Exception{  	
	  
	  String[] messageArray = message.split(" ");
	  if(message.equals("#quit")) {
		  quit();
	  }else if(message.equals("#stop")) {
		  stopListening();
		 
	  }else if(message.equals("#close")) {
		  this.sendToAllClients("WARNING - The server has stopped listening for connections SERVER SHUTTING DOWN! DISCONNECTING!");
		  close();
	  	
	  }else if(messageArray[0].equals("#setport")) {
		  if(getNumberOfClients() != 0 && isListening()) {
			  System.out.println("SERVER MSG> Already has port");
		  }else {
			  setPort(Integer.parseInt(messageArray[1]));
		  }
		  
	  }else if(message.equals("#start")) {
		  if(!isListening()) {
			  try {
				  listen();
			  }catch (IOException listen) {
				  System.out.println("SERVER MSG> Already is listening for clients");
			  }		  
		  }
	  }else if(message.equals("#getport")) {
	  		System.out.println("SERVER MSG> "+ getPort());
	  }
	  else {
		  try {
			 this.sendToAllClients("SERVER MSG>" + message);
			 System.out.println("SERVER MSG> " + message);
	  		}catch(Exception e2){
	  			clientUI.display("Could not send message to clients.  Terminating server.");
	  			quit();
	  			}
		  }
  }

  private void quit() {
	// TODO Auto-generated method stub
	  try
	    {
	      close();
	    }
	    catch(IOException e) {}
	    System.exit(0);
	
  }
  protected void serverClosed() {
	  System.out.println("Connection closed.");
		quit();	  
  }
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  
}
//End of EchoServer class

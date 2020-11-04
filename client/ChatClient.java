// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
	//add login as instance variable
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  //Add login as first parameter
  //if login is null and quits 
  //When opens connection send to server
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   * @throws Exception 
   */
  public void handleMessageFromClientUI(String message) throws Exception
  {  	  
  	//checks for commands otherwise sends to server
	
	
	String[] messageArray = message.split(" ");
	if(messageArray[0].equals("#sethost")) {
		if(isConnected()) {
			System.out.println("Already has host");	
			
		}else {
			setHost(messageArray[1]);
		}
		      	
	}else if(messageArray[0].equals("#setport")) {
		if(isConnected()) {
			System.out.println("Already has port");	
			
		}else {
			setPort(Integer.parseInt(messageArray[1]));
		}
			 
	}else if(message.equals("#quit")) {
  	quit();
  	}else if(message.equals("#logoff")) {
  		closeConnection();
  	}else if(message.equals("#login")) {
  		if(isConnected()) {
  			throw new Exception("already logged in");
  		}else{
  			openConnection();
  	}
  	}else if(message.equals("#gethost")) {
  		System.out.println(getHost());
  	}else if(message.equals("#getport")) {
  		System.out.println(getPort());
  	}
  	else {
  		try {
  			sendToServer(message);
  			}catch(IOException e2){
  				clientUI.display("Could not send message to server.  Terminating client.");
  				quit();
  			}
  		}
	}
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  	@Override 
  	public void connectionClosed() {
  		System.out.println("Connection closed.");
  		System.exit(0);
	}

	@Override
	public void connectionException(Exception exception){
		System.out.println("Connection closed.");
		quit();
  		
	}
}
//End of ChatClient class

import common.ChatIF;
import java.io.*;
import java.util.Scanner;
import client.ChatClient;

public class ServerConsole implements ChatIF {
	  //Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	  //Instance variables **********************************************
	  
	  /**
	   * The instance of the client that created this ConsoleChat.
	   */
	  EchoServer server;
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 
	  ChatIF clientUI;

	  
	  //Constructors ****************************************************
	
	  
	  //Add this as an input from ChatIF
	public ServerConsole(int port) {
	    try 
	    {
	      server = new EchoServer(port);
	      server.listen();
	      System.out.println("Server listening for connections on port " + port + ".");
	       
	    } 
	    catch(Exception e) 
	    {
	      System.out.println("Error: Can't setup connection!"
	                + " Terminating server.");
	      System.exit(1);
	    }
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }
	  
	public void accept() 
	  {
	    try
	    {
	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromClientUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
  
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
	}
	
	 public static void main(String[] args) 
	  {
	    int port = DEFAULT_PORT; 

	    try
	    {
	      port = Integer.parseInt(args[0]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      port = DEFAULT_PORT;
	    }
	    

	    ServerConsole serverInstance = new ServerConsole(port);
	    serverInstance.accept();  //Wait for console data
	  }

}
